import random as rand
import math as math
from state import State

class Character:
    instances = []
    def __init__(self, buildstring: str = None, given_name: str = None, middle_name: str = None, family_name: str = None, nameform: tup[int] = (0, 1, 2)):
        self.__class__.instances.append(self)

        self._given_name: str = given_name if given_name is not None else gen_given_name(self)
        self._middle_name: str = middle_name if middle_name is not None else gen_middle_name(self)
        self._family_name: str = family_name if family_name is not None else gen_family_name(self)
        # could maybe have a full or government name as well as preferred name fields
        self._nameform: tup = nameform
        self.full_name: dict = get_name(self)
        self.demographics: IDK = None
        self.age: int = 0 # the age in years of the character
        self.presentation: Presentation = None
        
        def gen_given_name(self) -> str:
            pass
            '''
            different demographic blocs affect the possible names
            presentation, heritage / ethnicity, generation, religion are considered
            each overlapping area has a weighted list for the most common names for those people

            determine if it's a compound first name like Jo Anne or John Paul
            compounds are sometimes hyphenated
            sometimes first names are used as an initial
            '''

        def gen_middle_name(self) -> str:
            if rand.randint(0,4) == 0: # one-in-five candidates will use their middle name(s) or initial(s)
                for i in range(rand.randint(1,2)): # the number of middle names to be used
                    pass
            return ""
            # determine if seveal middle names
            # determine if middle name is an initial
        
        def gen_last_name(self) -> str:
            pass
        
        def get_name(self):
            names: tup[str] = (self._given_name, self._middle_name, self._family_name)
            nameform = self._nameform
            return " ".join(names[nameform[0]], names[nameform[1]], names[nameform[2]])
        
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
    instances = [] # list containing all instances of candidate class
    def __init__(self, buildstring: str = None, is_player: bool = False):
        self.__class__.instances.append(self)
        Character.instances.append(self)
        
        self.delegates: int = 0 # number of delegates voting for the candidate
        self.cash: int = 0 # amount of cash held
        self.origin: State = None # the city of origin
        self.education: int = 0 # education level
        self.alignments: tup[int] = (1000,1000) # alignment on a 2-axis grid
        self.experience: list[] = [] # past experience
        self.skills: tup[int] = (0,0,0) # executive, legislative, judicial skill of the character - TODO could be a tuple
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
            forename = rand.choice(namesM)
            if rand.randint(0,2) == 0: # one-third of people will have a middle initial
                minitial = rand.choice(namesM)[0]
                forename += " " + minitial
        elif self.presentation == "F":
            forename = rand.choice(namesF)
            if rand.randint(0,2) == 0: # one-third of people will have a middle initial
                minitial = rand.choice(namesF)[0]
                forename += " " + minitial
        surname = rand.choice(surnames)
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
                ((-(12*self.age)/15)+140) if self.age >= 76 and self.age <= 100 else \
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
        return (
            "CH" + str(int(self.is_player)) +
            "-NA" + str(self.name) +
            "-DE" + str(self.delegates) +
            "-CA" + str(self.cash) +
            "-AG" + str(self.age) +
            "-PR" + str(self.presentation) +
            "-OR" + str(self.origin.name) +
            "-ED" + str(self.education) +
            "-EX" +
            "-AL" + str(sLen(self.alignments[0],4)) + str(sLen(self.alignments[1],4)) +
            "-SK" + str(sLen(self.skills[0],3)) + str(sLen(self.skills[1],3)) + str(sLen(self.skills[2],3)) + 
            "-CO" + str(self.conviction)
        )
        
    def __eq__(self, other): # returns true if the comparators are both Character instances and the name, alignments, and skills are the same
        return False if not isinstance(other, Character) else self.name == other.name and self.alignments == other.alignments and self.skills == other.skills

class Player(Candidate):
    instances = []
    def __init__(self):
        self.__class__.instances.append(self)
        Candidate.instances.append(self)
        Character.instances.append(self)

white_american_names = [ # namecensus.com/last-names/common-white-surnames/
    "Smith",
    "Johnson",
    "Miller",
    "Brown",
    "Jones",
    "Williams",
    "Davis",
    "Anderson",
    "Wilson",
    "Martin",
    "Taylor",
    "Moore",
    "Thompson",
    "White",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    "",
    ""
]

african_american_names = [ # from www.momjunction.com/articles/ghetto-baby-names-for-girls-and-boys_00398172/#african-american-boy-names and https://namecensus.com/last-names/common-black-surnames/
    "Williams",
    "Johnson",
    "Smith",
    "Jones",
    "Brown",
    "Jackson",
    "Davis",
    "Thomas",
    "Harris",
    "Robinson",
    "Taylor",
    "Wilson",
    "Moore",
    "White",
    "Lewis",
    "Walker",
    "Green",
    "Thompson",
    "Washington",
    "Anderson",
    "Scott",
    "Carter",
    "Wright",
    "Hill",
    "Allen",
    "Miller",
    "Mitchell",
    "Young",
    "Lee",
    "Martin",
    "Clark",
    "King",
    "Edwards",
    "Turner",
    "Coleman",
    "James",
    "Evans",
    "",
    "Ahmod",
    "Asaad",
    "Autry",
    "Booker",
    "Busta",
    "Calvin",
    "Caleb",
    "Cleavon",
    "Craig",
    "Dajon",
    "Daran",
    "Elijah",
    "Elroy",
    "Farrell",
    "Furnell",
    "Gabriel",
    "Guyton",
    "Hampton",
    "Herold",
    "Isiah",
    "Izaak",
    "Jamal",
    "Jaylen",
    "Joshua",
    "Karlus",
    "Kaseko",
    "Kwamie",
    "Latrell",
    "Lavar",
    "Leshawn",
    "Lloyd",
    "Luther",
    "Malik",
    "Major",
    "Nathan",
    "Noah",
    "Brian",
    "Owen",
    "Perry",
    "Parvez",
    "Pierre",
    "Qasim",
    "Roshaun",
    "Warren",
    "Xavier",
    "Yogi",
    "Zephan",
    "",
    "Akilah",
    "Amani",
    "Brianna",
    "Braelin",
    "Capria",
    "Cedrica",
    "Destiny",
    "Dallas",
    "Dazzline",
    "Edith",
    "Ezra",
    "Fatema",
    "Fayth",
    "Gabrielle",
    "Gail",
    "Haben",
    "Hazzell",
    "Hannah",
    "Hanita",
    "Indigo",
    "Imani",
    "Ida",
    "Jashanna",
    "Jada",
    "Kalisha",
    "Kimani",
    "Lafyette",
    "Lashonda",
    "Latanya",
    "Latasha",
    "Latonya",
    "Latricia",
    "Liana",
    "Makayla",
    "Marquita",
    "Merryll",
    "Nakala",
    "Navaeh",
    "Orlena",
    "Patriciana",
    "Rhianna",
    "Shania",
    "Shanika",
    "Shanique",
    "Shanita",
    "Talisa",
    "Sheniqua",
    "Talisha",
    "Tia",
    "Umbrosia",
    "Yolanda",
    "Zari"
]
    
historical_characters = [
    {
            'name': 'George Washington',
            'age': 57,
            'state': 'Virginia',
            'party': 'None'
    },
    {
            'name': 'John Adams',
            'age': 61,
            'state': 'Massachusetts',
            'party': 'Federalist'
    },
    {
            'name': 'Thomas Jefferson',
            'age': 57,
            'state': 'Virginia',
            'party': 'Democratic-Republican'
    },
    {
            'name': 'Andrew Jackson',
            'age': 61,
            'state': 'Tennessee',
            'party': 'Democrat'
    },
    {
            'name': 'Abraham Lincoln',
            'age': 52,
            'state': 'Illinois',
            'party': 'Republican'
    },
    {
            'name': 'Ulysses S. Grant',
            'age': 46,
            'state': 'Ohio',
            'party': 'Republican'
    },
    {
            'name': 'Theodore Roosevelt',
            'age': 42,
            'state': 'New York',
            'party': 'Republican'
    },
    {
            'name': 'Woodrow Wilson',
            'age': 56,
            'state': 'New Jersey',
            'party': 'Democrat'
    },
    {
            'name': 'Franklin D. Roosevelt',
            'age': 51,
            'state': 'New York',
            'party': 'Democrat'
    },
    {
            'name': 'Dwight D. Eisenhower',
            'age': 62,
            'state': 'Kansas',
            'party': 'Republican'
    },
    {
            'name': 'John F. Kennedy',
            'age': 43,
            'state': 'Massachusetts',
            'party': 'Democrat'
    },
    { # easter egg character
        'name' : 'Yelnick McWawa',
        'age' : 55, # average age
        'state' : 'Ohio', # average state
        'party' : 'Centrist' # average ideology
    }
]

list = [
    ["George Washington",57,"Virginia",3,[10,10],95],
    ["Thomas Jefferson",57,"Virginia"],
    ["Abraham Lincoln",52,"Illinois",4,[],80],
    ["Theodore Roosevelt",42,"New York",6,[],75],
    ["Woodrow Wilson",56,"New Jersey",8,[],50],
    ["Franklin D. Roosevelt",51,"New York",6,[],65],
    ["John F. Kennedy",43,"Massachusetts",6,[],60]
]

def sLen(value,length): # standardize length
    value = str(value)
    while len(value) < length:
        value = "0" + value
    return value
