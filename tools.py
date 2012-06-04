#! /usr/bin/python
import string

def getFrequency(target):
# return a dict contains the frequencies of alphabets appeared in the ciphertext;
# the argument is the ciphertext need to be analyzed.
    f_dict = {}
    for ch in target:
        if not f_dict.has_key(ch):
            f_dict.setdefault(ch, 1)
        else:
            f_dict[ch] += 1
    return f_dict

def getIndexOfCoincidence(f_dict, total_number):
# take a frequency dict and the total number of alphabets of the ciphertext corresponding to the dict
# return the INDEX OF COINCIDENCE of the ciphertext
    index_of_coincidence = 0.0
    for key in f_dict:
        index_of_coincidence += f_dict[key] * ( f_dict[key] - 1 )
    return index_of_coincidence / ( total_number * (total_number - 1))

def getDistribution(f_dict, total_number):
# take a frequency dict and the total number of alphabets of the ciphertext
# return a distribution dict of the corresponding ciphertext
    d_dict = {}
    for key in f_dict:
        d_dict.setdefault(key, f_dict[key] * 1.0 / total_number)
    return d_dict

def getNormalEnglishDistribution():
# return the normal meaningful English sentence distribution
    normal_dis_dict= {}
    normal_distribution = [.082, .015, .028, .043, .127, .022, .02, .061, .07,
                           .002, .008, .04, .024, .067, .075, .019, .001, .06,
                           .063, .091, .028, .01, .023, .011, .02, .001]
    normal_dis_dict = dict([c, normal_distribution[n]] for (n, c) in enumerate(string.lowercase[0:]))
    return normal_dis_dict

def getTransform(shift):
# get string transform for substitution cipher, shift indicates the transform step
    base_string = string.lowercase[0:]
    shift_string = string.lowercase[shift:] + string.lowercase[0:shift]
    transform = string.maketrans(shift_string, base_string)
    return transform

def getDistance(source, target):
# take two distribution dicts and calculate the distance between two strings corresponding to the dicts
# return the distance
    distance = .0
    for key in source:
        if target.has_key(key):
            distance += (target[key] - source[key]) ** 2
        else:
            distance += (0 - source[key]) ** 2
    return distance

def getGroupedCipher(target, key_period):
# arrange cipher based on the key period into 'key_period' rows, so each rows is a transform of the same alphabet
# return a dict contains 'key_period' keys, echo indicates a row.
    cipher_dict = {}
    for index, ch in enumerate(target):
        key = index % key_period
        if not cipher_dict.has_key(key):
            cipher_dict.setdefault(key, ch)
        else:
            cipher_dict[key] += ch
    return cipher_dict

def sortByValues(target):
# sort given dict by the value, instead of key. Return the sorted array of tuple based on the value in dict
    return sorted(target.iteritems(), key = lambda (key, value) : (value, key))

if __name__ == "__main__":
# for functional test only
    test_string = string.lowercase[0:]
    print getFrequency(test_string)


