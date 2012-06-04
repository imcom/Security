#! /usr/bin/python
import sys, string
from tools import sortByValues as sortV
from tools import getFrequency as getFreq
from tools import getIndexOfCoincidence as getIOC
from tools import getDistribution as getDistri
from tools import getDistance
from tools import getGroupedCipher as getG
from tools import getNormalEnglishDistribution as getN
from tools import getTransform as getT

cipher = "PPRFMGKVQOZMCIQREEOXERXENQATBSIWIRBSBZNAKIEVTEMEPARPZIYGURLIYQQRLMPENFCROISRBSPIXRPINQAGWFWBGYMWKEUVTILIFFQRCBUEWYCPYBVHKVBABLAENLBSBZNAKIDMIVAMPMQYWRZWAMWSWVQNAOALGUMQPWPNZIBWEGPIYCOHVXETUVAVABHEVADQPUPIKXGVUMOBVPIPHGNABMYQCNBIZEBHTHXMABTSJOREBLWVGJWAAMXFWJYWHEAIPPRJIVSIFAWXPWRALWKYHVKOHG"

dic_array = []
for i in range(3, 8): # iterate the key period
    dic_array.append(getG(cipher, i))

for d in dic_array:
#    print d.keys().__len__()
    for key in d:
        f_dict = getFreq(d[key])
#        print getIOC(f_dict, d[key].__len__())

target_cipher = getG(cipher.lower(), 6)

print '\nCiphertext:'
for v in target_cipher.values():
    print v

default_dic = getN()
default_str = string.lowercase[0:]

k_array = [k for k in default_str] # for display

res_list = []
for row in target_cipher.values():
    diff = [] # for display
    res = {}
    for i in range(0, 26):
        trans = getT(i)
        temp_str = row.translate(trans)
        f_dict = getFreq(temp_str)
        d_dict = getDistri(f_dict, temp_str.__len__())
        distance = getDistance(default_dic, d_dict)
        diff.append(distance)
        res.setdefault(default_str[i], distance)
    res_list.append(res)
#    print "-", "%5s - "*26 % tuple(k_array)
#    print "-", "%.3f - "*26 % tuple(diff)

print ''
answer = ''
for n, row in enumerate(res_list):
    print n, "row:"
    res_str = ''
    sorted_row = sortV(row)
    for k, v in sorted_row:
        res_str += "[%s]: %.3f, " % (k, v)
#    for item in sorted_row:
#        res_str += "[%s]: %.3f, " % (item[0], item[1])
    print res_str, '\n'
    answer += sorted_row[0][0]

print "Key: ", answer

plaintext = ''
pt_buf = []
for n, row in enumerate(target_cipher.values()):
    temp = map(lambda x: default_str.index(x), row) # switch to number to decrypt
    key = default_str.index(answer[n]) # get the numeric representation of key
    t_buf = []
    for item in temp:
        t_buf.append(default_str[(item - key) % 26])
    t_buf.reverse() # using pop to get the alphabet in oder later
    pt_buf.append(t_buf)

empty_array= [0, 0, 0, 0, 0, 0]
while True:
    flag = reduce(lambda x, y: x*y, empty_array) # when all bufs are empty, quit the loop
    if flag == 1:
        break
    for i, buf in enumerate(pt_buf):
        if not buf.__len__() == 0:
            plaintext += buf.pop()
        else:
            empty_array[i] = 1

print '\nPlaintext:'
print plaintext
print '\nHope Winnie had a happy ending...\n'

