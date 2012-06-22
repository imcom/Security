#include "stdio.h"
#include "gcrypt.h"

#define BUF_SIZE 32
#define BIT_MASK 0xfffff000

void displayBits(int target, int len);

int main(int argc, char *argv[]) {
    gcry_error_t err;
    gcry_md_hd_t hd;

    err = gcry_md_open(&hd, GCRY_MD_MD5, GCRY_MD_FLAG_SECURE);
    
    size_t digest_length = gcry_md_get_algo_dlen(GCRY_MD_MD5);
    unsigned char *digest;

    digest = gcry_xmalloc(digest_length);

    int *m, index, j, loop_num = 1, chains_exp;
    if (argc > 1) {
        chains_exp = atoi(argv[1]);
    } else {
        chains_exp = 16;
    }

    int bits[BUF_SIZE];
    for (index = 0; index < BUF_SIZE; ++index) {
        bits[index] = 0;
    }
    for (index = 0; index < chains_exp; ++index) {
        loop_num *= 2; 
    }

    for (j = 0; j < loop_num; j++) {
        m = gcry_random_bytes(4, GCRY_VERY_STRONG_RANDOM);// get 32 bits random number
        *m &= BIT_MASK;
        //displayBits(m, BUF_SIZE); // display random K0
        for (index = 0; index < 256; index++) {
            gcry_md_write(hd, m, BUF_SIZE);

            digest = gcry_md_read(hd, 0);

            *m = (digest[3] << 24) | (digest[2] << 16) | (digest[1] << 8) | digest[0];
            *m &= BIT_MASK;

            displayBits(*m, BUF_SIZE);// display hash result
            *m ^= index; // for Exercise 17

            gcry_md_reset(hd);
        }
    }

    gcry_md_close(hd);
    return 0;
}

void displayBits(int target, int len) {
    int i, bit;
    for (i = len - 1; i >= 0; --i) {
        bit= (target & (1 << i)) ? 1 : 0;
        printf("%d ", bit);
    }
    putchar('\n');
}
