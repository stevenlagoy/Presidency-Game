from datetime import datetime

def error_log(logline: str) -> None:
    '''Writes the specified logline into "log.txt". Pass a string containing information about the error and the place it was created.'''
    log_file = open("log.txt", "a")
    log_file.write(get_timestamp() + " - " + logline + "\n")
    log_file.close()
    return None

def get_timestamp() -> str:
    ''' Returns the current date and time as a string. To be used for logging applications. '''
    return datetime.now().strftime("%d/%m/%Y %H:%M:%S")