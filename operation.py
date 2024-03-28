from typing import List

class Operation:
    instances: List = []
    def __init__(self, operator, agents):
        self.__class__.instances.append(self)
        self.operator = operator # character object associated as the primary character for this operation
        self.agents = agents # list of characters assigned to influence this operation
        self.events_history = {} # list of events and probabilities that occurred throughout the course of the operation
        self.probability_weight = 0
    

def Campaign(Operation):
    instances = []
    def __init__(self):
        self.__class__.instances.append(self)

def GatherIntel(Operation):
    instances = []
    def __init__(self, aggressiveness, directness, secrecy):
        self.__class__.instances.append(self)

'''
All possibilities:
Agg Dir Sec

000
001
002
010
011
012
020
021
022
100
101
102
110
111
112
120
121
122
200
201
202
210
211
212
220
221
222
'''