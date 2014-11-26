from __future__ import division
from svm import *
import random
import numpy as np

class MySvm:
    def __init__(self, file_train, file_test):
        self.training_labels = self.load_labels(file_train)
        self.training_data = self.load_data(file_train)
        self.test_labels = self.load_labels(file_test)
        self.test_data = self.load_data(file_test)
        self.scores = []
        self.num_support_vectors = 0

    def load_labels(self, file_name):
        file_obj = open(file_name)
        labels = []
        for line in file_obj:
            labels.append(float(line.split()[0]))
        return labels

    def load_data(self, file_name):
        file_obj = open(file_name)
        data = []
        for line in file_obj:
            item = []
            line_split = line.split()
            for i in range(1, len(line_split)):
                item.append(float(line_split[i]))
            data.append(item)
        return data

    def one_versus_all(self, num_one, error_const, poly_degree):
        scores = []
        for label in self.training_labels:
            if label == num_one:
                scores.append(1)
            else:
                scores.append(0)
        prob = svm_problem(scores, self.training_data)
        param = svm_parameter('-t 1 -c .01 -d 2')
        model = svm_train(prob, param)

        clf = svm.SVC()
        clf.kernel = 'poly'
        clf.degree = poly_degree
        clf.C = error_const
        clf.fit(self.training_data, scores)
         Set object variables
        print 'len support_vectors_ = ', len(clf.support_vectors_)
        print 'len support_ = ', len(clf.support_)
        print 'len n_support_ = ', clf.n_support_[0]
        self.num_support_vectors = len(clf.support_vectors_)
        self.clf = clf
        self.scores = scores

    def get_error_in(self):
        count_misclassified = 0
        for i in range(0, len(self.training_data)):
            item = self.training_data[i]
            prediction = self.clf.predict(item)[0]
            print 'prediction = ', prediction, ' and score = ', scores[i]
            if prediction != self.scores[i]:
                count_misclassified += 1
        return count_misclassified / len(self.training_data)

if __name__ == '__main__':
    print 'Kshit is gay'
    my_svm = MySvm('features.train', 'features.test')
    #for i in range(1, 11, 2):
        #my_svm.one_versus_all(i, .01, 2)
        #error_in = my_svm.get_error_in()
        #print 'Error in for ', i, ' versus all = ', error_in
