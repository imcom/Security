#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <iostream>
#include <fstream>
#include <list>
using namespace std;

#define LENGTH 32
#define KEY_MASK 0x000000ff

void displayBits(long target, int len) {
    int i, bit;
    for (i = len - 1; i >= 0; --i) {
        bit= (target & (1 << i)) ? 1 : 0;
        printf("%d ", bit);
    }
    putchar('\n');
}

long update(long current_state) { // update the inner state
    long tmp;
    long modulo = 1;
    modulo = modulo << LENGTH; // 2 ^ 32
    tmp = (current_state * 69069 + 5) % modulo;
    return tmp;
}

void getKey(char *key, long init_state) { // given a init state, generate the key
    long state;
    state = update(init_state);
    int i = 0;
    for ( ; i < 16; ++i) {
        key[i] = state & KEY_MASK;
        state = update(state);
    }
}

int main(int argc, char **argv) {
    time_t start_time, end_time;
    struct tm *start_date, *end_date;
    long state;
    char key[16];

    start_date = (tm *) malloc(sizeof(tm));
    end_date = (tm *) malloc(sizeof(tm));

    start_date->tm_year = 2009;
    end_date->tm_year = 2009;

    start_date->tm_mon = 6;
    end_date->tm_mon = 6;

    start_date->tm_mday = 22;
    end_date->tm_mday = 29;

    start_time = mktime(start_date); // set starting time of key gerenration
    end_time = mktime(end_date); // set ending time of key generation

    ifstream is;
    is.open("ciphertext_sheet3.txt", ios::binary);

    char *cipher;
    is.seekg(0, ios::end);
    int length = is.tellg();
    is.seekg(0, ios::beg);

    cipher = new char[length];
    is.read(cipher, length); // read cipher text from file
    is.close();

    long i;
    bool false_flag = false;
    list<long> keys;
    for ( i = start_time; i <= end_time; ++i) { // enumerate all possible keys, keys are duplicate here actually.
        getKey(key, i);
        int j;
        char plaintext[16]; // firstly, just try the first group of ciphertext, 16 bytes. If they do not make sense then the key is wrong
        for ( j = 0; j < 16; ++j) {
            char ch;
            ch = cipher[j] ^ key[j];
            if ( j == 0 ) {
                if (!isupper(ch)) { // assume the first letter should be in Capital
                    false_flag = true;
                    break;
                }
                plaintext[j] = ch;
            } else {
                if (isalnum(ch) || ispunct(ch) || isspace(ch)) { // the following characters should be letter/punctuation/space
                    plaintext[j] = ch;
                } else {
                    false_flag = true;
                    break;
                }
            }
        } // for loop (plaintext)
        if ( !false_flag) {
            keys.push_back(i);
        } else { // if non-alpha, non-number, non-punctuation, non-space char found, then the current key is wrong.
            false_flag = false;
        }
    } // for loop (all starting state)

    getKey(key, keys.front()); // pick any of those init states and get a key
    int j;
    char plaintext[length+1];
    for ( j = 0; j < length; ++j) {
        plaintext[j] = cipher[j] ^ key[j % 16]; // decryption
    }
    plaintext[j] = NULL;

    cout << plaintext << endl;

    return 0;
}
