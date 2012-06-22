#include "iostream"
#include "string"
#include "gcrypt.h"
#include "tr1/unordered_map"
#include "set"
#include "list"
#include "fstream"
#include "sstream"
using namespace std;

#define BUFF_SIZE 32
#define H28_MASK 0xfffffff0 // bit-wise operation for get most significant 28 bits of a byte
#define H4_MASK 0xf0000000 // bit-wise operation for get most significant 4 bits of a byte
#define L4_MASK 0x0f // bit-wise operation for get least significant 4 bits of a byte
#define CHALLENGE 518330736
#define TABLE_SIZE 6
#define CHAIN_LENGTH 10

void displayBits(unsigned int target, int len);
void getHash(unsigned int *s, gcry_md_hd_t hd, unsigned char *digest, unsigned int *r, int salt);
void getSecret(unsigned int *s, unsigned int *concat);
void generateRainbow();
void crack(unsigned int *s);

int main(int argc, char **argv) {

    gcry_check_version(NULL);
    unsigned int *s; // random secret s
    s = (unsigned int *) gcry_xmalloc(4);
    s = (unsigned int *) gcry_random_bytes(4, GCRY_VERY_STRONG_RANDOM);// get 32 random bits
    //generateRainbow();
    //*s = 3539703360; // a key
    //*s = 1722261872; // false alarm
    crack(s);

    return 0;
}

void generateRainbow() { // generate rainbow table and store it to a file
    std::tr1::unordered_map< int, list<int> > rainbow;
    int i, j;
    gcry_error_t err;
    gcry_md_hd_t hd;
    int chain_length = 1, table_size = 1;
    unsigned int r = 0;
    set<int> sp_set;
    set<int>::iterator sp_finder;
    set<int> point_set;
    unsigned int *s, *concat;

    concat = (unsigned int *) gcry_xmalloc(8);
    err = gcry_md_open(&hd, GCRY_MD_MD5, GCRY_MD_FLAG_SECURE);
    size_t digest_length = gcry_md_get_algo_dlen(GCRY_MD_MD5);
    unsigned char *digest;
    digest = (unsigned char *) gcry_xmalloc(digest_length);
    std::tr1::unordered_map< int, list<int> >::const_iterator ep_finder;

    for (i = 0; i < CHAIN_LENGTH; ++i) {
        chain_length *= 2; 
    }

    for (i = 0; i < TABLE_SIZE; ++i) {
        table_size *= 2;
    }

    unsigned int tmp;
    for (i = 0; i < table_size; ++i) {
        s = (unsigned int *) gcry_random_bytes(4, GCRY_VERY_STRONG_RANDOM);// get 32 random bits
        //cout << "k0: " << *s << endl;
        getSecret(s, concat); // get 56 bits concatenation
        sp_finder = sp_set.find(*s);
        if ( sp_finder != sp_set.end() ) { // if the init random duplicate, then just drop the row
            //i--;
            continue;
        }
        sp_set.insert(*s);
        for (j = 0; j < chain_length; ++j) {
            getHash(concat, hd, digest, &r, j); // get hash result r
            point_set.insert(r); // records different points
            tmp = r << 4; // set least significant 4 bits to zero
            cout << "i: " << i << " j: " << j << " k = " << tmp << endl;
            getSecret(&tmp, concat);
        } // generation of each chain
        ep_finder = rainbow.find(r); // use EP as key in unordered map
        if (ep_finder == rainbow.end()) {
            list<int> sp_list; // in case multiple SPs lead to a same EP
            sp_list.push_back(*s);
            rainbow[r] = sp_list; // set r as key, associate with a SP list
        } else {
            rainbow[r].push_back(*s);
        }
    } // generation of rainbow
    sp_set.clear(); // free up mem

    cout << "Rainbow table size: " << rainbow.size() << endl;
    cout << "Total points: " << point_set.size() << endl;
    point_set.clear();

    ofstream fout;
    fout.open("rainbow_table");
    list<int> results;

    ep_finder = rainbow.begin();
    string buf;
    char point[32];
    // write rainbow table to a file
    for (; ep_finder != rainbow.end(); ++ep_finder) {
        sprintf(point, "%d ", ep_finder->first);
        buf += point;
        results = ep_finder->second;
        while ( !results.empty() ) {
            sprintf(point, "%d ", results.front());
            buf += point;
            results.pop_front();
        }
        fout << buf.substr(0, buf.size() - 1) << "\n";
        buf = "";
    }

    fout << flush;
    fout.close();
} // generateRainbow

void crack(unsigned int *s) { // get a parameter as the secret is going to be cracked and try to match it via rainbow table
    std::tr1::unordered_map< int, list<int> > rainbow;
    ifstream fin;
    fin.open("rainbow_table");
    list<int> row;
    int p;
    string item, buf;
    // get previously generated rainbow table from a file
    while( getline(fin, buf) ) {
        stringstream ss_row(buf);
        while ( getline(ss_row, item, ' ') ){
            stringstream ss_item(item);
            ss_item >> p;
            row.push_back(p);
        }
        list<int> sp_list; // in case multiple SPs lead to a same EP
        p = row.front();
        rainbow[p] = sp_list; // set EP as key, associate with a SP list
        row.pop_front();
        while (!row.empty()) {
            rainbow[p].push_back(row.front());
            row.pop_front();
        }
    }

    fin.close();

    gcry_error_t err;
    gcry_md_hd_t hd;
    err = gcry_md_open(&hd, GCRY_MD_MD5, GCRY_MD_FLAG_SECURE);
    std::tr1::unordered_map< int, list<int> >::const_iterator ep_finder;
    unsigned int *concat;
    concat = (unsigned int *) gcry_xmalloc(8);
    getSecret(s, concat); // s is from the key fob, NOTICE it is unknown to the attacker
    size_t digest_length = gcry_md_get_algo_dlen(GCRY_MD_MD5);
    unsigned char *digest;
    digest = (unsigned char *) gcry_xmalloc(digest_length);
    unsigned int received_r = 0;
    unsigned int r = 0;
    unsigned int key = 0;
    int i, j;
    int chain_length = 1;
    list<int> sp_list;
    unsigned int sp = 0;
    getHash(concat, hd, digest, &r, 0); // get the "original" r from the fob
    received_r = r;
    // using "original" r to generate its successors
    for ( i = 0; i < CHAIN_LENGTH; ++i) {
        chain_length *= 2;
    }
    for ( i = chain_length - 1; i >= 0; --i) { // start from the last column
        //cout << "Starting from: [" << i << "] column..." << endl;
        r = (r + i) % (1 << 28);
        //cout << "i: " << i << " R: " << r << endl;
        for ( j = i + 1; j < chain_length; ++j) {
            key = r << 4; // r uses least significant 28 bits, so need to shift to left 4 bits before using as secret
            //cout << "i: " << i << " j: " << j << " key: " << key << endl;
            getSecret(&key, concat);
            getHash(concat, hd, digest, &r, j);
        } // iteration of r's successor
        ep_finder = rainbow.find(r); // find r in rainbow table
        if (ep_finder == rainbow.end()) { // if not found, move to the previous column
            //cout << " not found!" << endl;
            r = received_r;
            continue;
        } else {
            unsigned int ckey;
            sp_list = rainbow[r];
            while (!sp_list.empty()) {
                sp = sp_list.front(); // get the first item in the list
                sp_list.pop_front(); // remove the first item in the list
                getSecret(&sp, concat); // set K0
                for ( j = 0; j < i; ++j) {
                    getHash(concat, hd, digest, &r, j); // r is 32bits with 0000 as highest four bits
                    key = r << 4;
                    getSecret(&key, concat);
                }
                ckey = (r << 4) & H28_MASK;
                //cout << "Verification - i: " << i << " j: " << j << " key: " << ckey << endl;
                if (!(ckey ^ *s)) {  // verification of the candidate key
                    cout << "Candidate: " << ckey << " ";
                    getSecret(&ckey, concat);
                    getHash(concat, hd, digest, &r, 0);
                    if ( r == received_r ) {
                        cout << "Verification: True" << endl;
                    } else {
                        cout << "Verification: Fasle" << endl;
                    }
                } else {
                    cout << "False alarm! " << ckey << endl;
                }
            } // while enumerate SPs
            r = received_r; // reset r to "original" one
        } // if-else
    } // for-loop column of rainbow
    cout << "Lookup finished" << endl;
} // crack

void getSecret(unsigned int *s, unsigned int *concat) {
    *s &= H28_MASK;
    concat[1] = *s | ((CHALLENGE & H4_MASK) >> 28);
    concat[0] = (CHALLENGE & H28_MASK) << 4;
}

void getHash(unsigned int *concat, gcry_md_hd_t hd, unsigned char *digest, unsigned int *r, int salt) {
    gcry_md_write(hd, concat, 8); // 8 bytes, 64 bits in total
    digest = gcry_md_read(hd, 0);
    *r = ((digest[3] & L4_MASK) << 24) | (digest[2] << 16) | (digest[1] << 8) | digest[0];
    *r = (*r + salt) % (1 << 28);
    gcry_md_reset(hd);
}

void displayBits(unsigned int target, int len) {
    int i, bit;
    for (i = len - 1; i >= 0; --i) {
        bit= (target & (1 << i)) ? 1 : 0;
        printf("%d ", bit);
    }
    putchar('\n');
}


