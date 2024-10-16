import random as rand
import math as math
from os import listdir
from typing import List, Tuple, NoReturn, Dict
from engine import error_log, percentage_roll
from state import State

# read the names file and create a local dictionary
def read_names(file_name: str) -> List[str] | None:
    ''' Reads a passed names file and returns the read names as a list. Raises exception if the file is not found or is improperly formatted. '''
    
    # open the file and read the contents
    try:
        default_path: str = "\\".join(__file__.split("\\")[:-1]) + "\\names\\"
        contents: List[str] = []
        file = open(default_path + file_name, "r")
        contents = file.read().split("\n")
    except FileNotFoundError: # log and return None if file does not exist
        error_log("Failed to open file \"" + file_name + ".txt\" in read_names() in \"character.py\". Path to file is invalid, or file does not exist.")
        return None
    
    # check that the file is properly formatted and readable
    try:
        contents = contents[contents.index("&&&")+1:] # sort out unwanted lines
    except ValueError: # log and return if file is improperly formatted
        error_log("Failed to read file \"" + file_name + ".txt\" in read_names() in \"character.py\" File is not properly formatted (include \"&&&\" line to mark start of readable content in file).")
        return None
    
    return contents

names: Dict = {}
for file in listdir("names\\"):
    names[file.split(".")[0]] = read_names(file)

class Character:
    instances: List = []
    def __init__(self, buildstring: str = None, *, given_name: str = None, middle_name: str = None, family_name: str = None, nameform: Tuple[int] = (0, 1, 2)):
        self.__class__.instances.append(self)

        self._given_name: str = given_name if given_name else self.gen_given_name() # sets the given name to the passed name OR generates one
        self._middle_name: str = middle_name if middle_name else self.gen_middle_name() # sets the middle name to the passed name OR generates one
        self._family_name: str = family_name if family_name else self.gen_family_name() # sets the family name to the passed name OR generates one
        # could maybe have a full or government name as well as preferred name fields
        self._nameform: Tuple[int] = nameform
        #self.full_name: dict = self.get_name(self)
        self.demographics = None
        self.age: int = 0 # the age in years of the character
        self.presentation: str = None

    def gen_given_name(self) -> str:
        ''' Generate the character's given (first) name. '''
        given_name: str = ""
        given_name += rand.choice(names.get("names_given"))
        '''
        different demographic blocs affect the possible names
        presentation, heritage / ethnicity, generation, religion are considered
        each overlapping area has a weighted list for the most common names for those people

        determine if it's a compound first name like Jo Anne or John Paul
        compounds are sometimes hyphenated
        sometimes first names are used as an initial
        '''
        return given_name

    def gen_middle_name(self) -> str:
        ''' Generate the character's middle name. '''
        middle_name: str = ""
        if percentage_roll(0.8): # 80% of characters will have at least one middle name
            middle_name += rand.choice(names.get("names_given"))
            if percentage_roll(0.125): # 10% of people will have two middle names
                middle_name += " " + rand.choice(names.get("names_given"))
        return middle_name
    '''
    about 30% of presidents are known by their middle name or initial
    38% of government officials use a middle initial as of 2014 www.nytimes.com/2014/07/13/fashion/theyre-dropping-like-middle-initials.html
    '''

    def gen_family_name(self) -> str:
        ''' Generate the character's family (last) name. '''
        last_name: str = ""
        last_name += rand.choice(names.get("names_family"))
        return last_name
    
    def get_name(self) -> str:
        ''' Return the name of a character according to their nameform. '''
        names: Tuple[str] = (self._given_name, self._middle_name, self._family_name)
        nameform = self._nameform
        return " ".join(names[nameform[0]], names[nameform[1]], names[nameform[2]])
    
    def __repr__() -> str:
        ''' Return a string representation of the character which can be used as a buildstring. '''
        return f""

    def __eq__(self, other) -> bool:
        ''' Returns true if all the datafields between two characters have the same value. Returns false otherwise.'''
        try:
            return self.__repr__ == other.__repr__
        except ValueError:
            error_log(f"Attempted to compare mismatched-type objects: {self} of type {type(self)}, {other} of type {type(other)}")
            return False

    '''
    possibilities for names:
    Firstname Lastname
    Firstname Middlename Lastname
    Firstname Minitial Lastname    also sometimes names can just be a single letter without being initials, like Harry S Truman
    Familyname Givenname
    Firstname Lastname Jr/Sr
    Firstname Lastname Number
    Firstinitial Middlename Lastname
    Also titles like Mr. Ms. Mrs. Dr.   maybe these shouldn't be tracked...
    should maiden names be tracked? this could be relevant for divorces

    perhaps users select a format for their name:
    Given Middle Family
    Family Given
    These name categories may include several words, abbreviations, etc or may be blank
    
    Given Middle Family: John Quincy Adams
    Given Family: George Washington
    Given Middle(initial) Family: Ulysses S. Grant
    Given Middle(initials) Family: George H. W. Bush
    Given(initial) Middle(initial) Family: H. G. Wells
    '''

class Candidate(Character):
    instances: List = []
    def __init__(self, buildstring: str = None, is_player: bool = False):
        self.__class__.instances.append(self)
        Character.instances.append(self)
        
        self.delegates: int = 0 # number of delegates voting for the candidate
        self.cash: int = 0 # amount of cash held
        self.origin: State = None # the city of origin
        self.education: int = 0 # education level
        self.alignments: Tuple[int] = (1000,1000) # alignment on a 2-axis grid
        self.experience: List = [] # past experience
        self.skills: Tuple[int] = (0,0,0) # executive, legislative, judicial skill of the character - TODO could be a tuple
        self.aptitude: int = 0 # sum of all the skills
        self.conviction: int = 0 # conviction / stagnation of the character's politics
        self.ageMod: float = 0
        
        self.genProfile()

    def genProfileInput(self) -> None:
        # get player name
        self.setNameInput()
        
        # check if player wants to use a historical character
        for character in historical_characters:
            if self.name == character[0]:
                if input("This name is associated with a historical character. Would you like to continue with the predetermined stats? (Y/N):").upper() == "Y":
                    return None # this is where the rest of the profile will be generated for the player
                else:
                    break

        self.setAgeInput() # get player age
        self.setOriginInput() # get player state origin
        self.setEducationInput() # get player education level
        self.setAlignmentInput() # get player alignment
        # get player experience
        self.rollSkills() # roll player skills
        self.rollConviction() # roll player conviction
        
    def genProfile(self):
        self.genPresentation()
        self.genName()
        self.genAge()
        self.genOrigin()
        self.genEducation()
        self.rollSkills()
        self.rollConviction()
        
    def setNameInput(self):
        # set the player's name from input
        self.name = None
        while self.name is None:
            try:
                self.name = input("Your player character's name (first, middle initial (optional), and last):\n")
                if self.name == "":
                    print("You must input at least a first and last name in valid letters")
                    self.name = ""
                elif self.name.split(" ")[1] == "":
                    print("You must input at least a first and last name in valid letters")
                    self.name = ""
            except:
                print("You must input a first and last name in valid letters")
                self.name = ""
        normalizedName = []
        normalizedName.append(self.name.split(" ")[0].title())
        for name_ in self.name.split(" "):
            if name_.title() in normalizedName:
                pass
            else:
                normalizedName.append(name_.upper())
        self.name = " ".join(normalizedName)
        if input("Your candidate's name is: " + self.name + "\nContunue with this name? (Y/N):\n").upper() != "Y":
            self.setNameInput()
        else:
            return self.name
            
    def genName(self):
        if self.presentation == "M":
            forename = rand.choice(names["names_given_white"])
            if rand.randint(0,2) == 0: # one-third of people will have a middle initial
                minitial = rand.choice(names["names_middle_white"])[0]
                forename += " " + minitial
        elif self.presentation == "F":
            forename = rand.choice(names["names_given_white"])
            if rand.randint(0,2) == 0: # one-third of people will have a middle initial
                minitial = rand.choice(names["names_given_white"])[0]
                forename += " " + minitial
        surname = rand.choice(names["names_given_white"])
        self.name = forename + " " + surname
        return self.name
                
    def setAgeInput(self):
        while self.age < 35 or self.age > 100:
            try:
                self.age = int(input("Your character's age (between 35 and 100): "))
                if self.age < 35 or self.age > 100:
                    print("Your age must be between 35 and 100 (inclusive)") # may add other ages later
            except ValueError:
                print("You must input a valid age between 35 and 100")
        self.ageMod = \
            None if self.age < 35 else \
            (-(((self.age-55)**2)/20)+100) if self.age >= 35 and self.age <= 75 else \
            ((-(12*self.age)/15)+140) if self.age >= 76 and self.age <= 100 else \
            None
        return self.age
            
    def genAge(self):
        ages = range(35,100)
        weights = [1, 1, 2, 3, 3, 3, 4, 5, 6, 7, 9, 11, 13, 15, 17, 19, 22, 25, 25, 25, 23, 23, 21, 19, 17, 15, 13, 12, 12, 11, 10, 9, 8, 7, 7, 5, 4, 3, 3, 3, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]
        # there's probably a better way to do this... except rand.choices() is broken or unimplemented
        weighted_ages = []
        '''
        weighted ages should contain entries for each age equal to the number at that index in weights
        '''
        for i in range(len(weights)):
            for j in range(weights[i]):
                weighted_ages.append(ages[i])
        self.age = rand.choice(weighted_ages)
        self.ageMod = \
                None if self.age < 35 else \
                (-(((self.age-55)**2)/20)+100) if self.age >= 35 and self.age <= 75 else \
                ((-(4*self.age)/5)+140) if self.age > 75 and self.age <= 100 else \
                None
        return self.age
    
    def genPresentation(self):
        self.presentation = rand.choice(["M","F"])
    
    def setOriginInput(self):
        # will take an input and match it to the name or abbreviation of a state
        while self.origin is None:
            try:
                self.origin = input("Your character's state of origin (US State/Territory full name or abbreviation): ").title()
            except ValueError:
                print("You must input the valid full name or two-letter abbreviation of a US state or incorporated territory")
                self.origin = None
                continue
            for state in State.instances:
                if self.origin.upper() == state.name.upper() or self.origin.upper() == state.abb.upper():
                    self.origin = state
                    if input("Your candidate's state of origin is: " + self.origin.name + "\nContinue with this origin? (Y/N)").upper() != "Y":
                        self.origin = None
                        break
                    else:
                        return self.origin
        print("You must input the valid full name or two-letter abbreviation of a US state or incorporated territory")
        self.origin = None
    
    def genOrigin(self):
        states = State.instances
        populations = [math.floor(state_.population/500000) for state_ in State.instances] # divide population by set amount to prevent list of billions of state objects
        weighted_states = []
        for i in range(len(populations)):
            for j in range(populations[i]):
                weighted_states.append(states[i])
        self.origin = rand.choice(weighted_states)

    def setEducationInput(self):
        educationDescriptions = [
            "1 - Primary Education",
            "2 - Lower Secondary Education",
            "3 - Upper Secondary Education",
            "4 - Post-secondary Education",
            "5 - Tertiary Education",
            "6 - Bachelors Degree",
            "7 - Masters Degree",
            "8 - Doctoral Degree"
            ]
        self.education = 0
        while self.education > 8 or self.education < 1:
            try:
                for description in educationDescriptions:
                    print(description)
                self.education = int(input("Education level (1 to 8):\n"))
            except:
                print("You must input an integer between 1 and 8")
                self.education = 0
        
        return self.education
    
    def genEducation(self):
        levels = range(1,9)
        weights = [1, 20, 68, 279, 149, 105, 235, 144] # according to US Census Bureau 2022
        weighted_levels = []
        for i in range(len(weights)):
            for j in range(weights[i]):
                weighted_levels.append(levels[i])
        self.education = rand.choice(weighted_levels)
        return self.education
    
    def setAlignmentInput(self):
        self.alignment = [1000,1000]
        while self.alignment[0] > 100 or self.alignment[0] < -100 or self.alignment[1] > 100 or self.alignment[1] < -100:
            try:
                self.alignment[0] = int(input("Input your character's left-right alignment (-100 to 100):\n"))
                self.alignment[1] = int(input("Input your character's auth-lib alignment (-100 to 100):\n"))
            except:
                print("You must input a valid integer from -100 to 100")
                self.alignment = [1000,1000]
        return self.alignment
        
    def rollSkills(self):
        self.skills = [0,0,0]
        self.skills[0] = math.floor((rand.randint(0,8) * self.education - rand.randint(0,self.education)) * self.ageMod / 100)
        self.skills[1] = math.floor((rand.randint(0,8) * self.education - rand.randint(0,self.education)) * self.ageMod / 100)
        self.skills[2] = math.floor((rand.randint(0,8) * self.education - rand.randint(0,self.education)) * self.ageMod / 100)
        
        for i in range(len(self.skills)): # checks that all skills are at least 1
            self.skills[i] = 1 if self.skills[i] <= 0 else self.skills[i]
            
        self.aptitude = sum(self.skills)
        
        return self.skills, self.aptitude
        
    def setSkills(self,amounts):
        # amounts is a list of three ints
        if len(amounts) == 3 and type(amounts) is list:
            self.skills = amounts
            self.aptitude = sum(self.skills)
            return amounts
        else:
            return False
        
    def rollConviction(self):
        self.conviction = rand.randint(0,100)
        return self.conviction
    
    def __str__(self): # overload str()
        return self.name
    
    def __repr__(self): # overload for representation func - creates buildstrings
        ...
        
    def __eq__(self, other): # returns true if the comparators are both Character instances and the name, alignments, and skills are the same
        return False if not isinstance(other, Character) else self.name == other.name and self.alignments == other.alignments and self.skills == other.skills

class Player(Candidate):
    instances: List = []
    def __init__(self):
        self.__class__.instances.append(self)
        Candidate.instances.append(self)
        Character.instances.append(self)

        self.family = {
            "ancestors" : [],
            "grandparents" : [],
            "parents" : [],
            "piblings" : [],
            "siblings" : [],
            "cousins" : [],
            "children" : [],
            "niblings" : [],
            "grandchildren" : [],
            "descendents" : [],
            "spouses" : []
        }

historical_characters: List[Dict] = [
    {
    },
]