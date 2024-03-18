from datetime import datetime
import random as rand

def error_log(logline: str) -> None:
    '''Writes the specified logline into "log.txt". Pass a string containing information about the error and the place it was created.'''
    log_file = open("log.txt", "a")
    log_file.write(get_timestamp() + " - " + logline + "\n")
    log_file.close()
    return None

def get_timestamp() -> str:
    ''' Returns the current date and time as a string. To be used for logging applications. '''
    return datetime.now().strftime("%d/%m/%Y %H:%M:%S")

def percentage_roll(chance: float, affirmValue: bool = True) -> bool:
    ''' Returns boolean randomly based on the passed percentage chance. An affirmative roll will return affirmValue. '''

    if chance >= 1: return affirmValue # if the chance is 100% or greater, always True
    elif chance <= 0: return not affirmValue # if the chance is 0% or less, always False
    else:
        return affirmValue if rand.uniform(0, 1) <= chance else not affirmValue