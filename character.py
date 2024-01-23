import random as rand
import math as math
from state import State

class Character:
  instances = [] # list containing all instances of character class
  def __init__(self, string = None, is_player = False):
    self.__class__.instances.append(self)
    
    if string is not None: # if a character build string is passed
      attributes = string.split("-")
      tags = [tag[:2] for tag in attributes]
      self.is_player = int(attributes[tags.index("CH")][2:]) == 1
      self.name = attributes[tags.index("NA")][2:]
      self.delegates = attributes[tags.index("DE")][2:]
      self.cash = attributes[tags.index("CA")][2:]
      self.age = attributes[tags.index("AG")][2:]
      self.presentation = attributes[tags.index("PR")][2:]
      self.origin = [state for state in State.instances if state.name == str(attributes[tags.index("OR")][2:])][0]
      self.education = attributes[tags.index("ED")][2:]
      self.alignments = attributes[tags.index("AL")][2:]
      self.experiences = attributes[tags.index("EX")][2:]
      self.skills = [int(attributes[tags.index("SK")][2:5]),int(attributes[tags.index("SK")][5:8]),int(attributes[tags.index("SK")][8:11])]
      self.aptitude = sum(self.skills)
      self.conviction = attributes[tags.index("CO")][2:]
      self.ageMod = 0

    elif is_player is True: # if constructing a player character without a buildstring
      self.is_player = True
      self.delegates = 0 # number of delegates voting for the player
      self.cash = 0 # amount of cash held by the player
      self.name = "" # name of the player
      self.presentation = None
      self.age = 0 # age of the player
      self.origin = None # state object which represents the player's homestate
      self.education = 0 # education level of the player
      self.alignments = [1000,1000] # alignment on a 2-axis grid of the player
      self.experience = [] # past experience of the player
      self.skills = [0,0,0] # executive, legislative, judicial skill of the player - TODO could be a tuple
      self.aptitude = 0 # sum of all the skills
      self.conviction = 0 # conviction / stagnation of the player's politics
      self.ageMod = 0
    
      self.genProfileInput()
      
    elif is_player is False: # if constructing a nonplayer character without a buildstring
      self.is_player = False
      self.delegates = 0 # number of delegates voting for the character
      self.cash = 0 # amound of cash held by the character
      self.name = "" # name of the character
      self.presentation = None
      self.age = 0 # age of the character
      self.origin = None
      self.education = 0 # education level of the character
      self.alignments = [1000,1000] # alignment on a 2-axis grid of the character
      self.experience = [] # past experience of the character
      self.skills = [0,0,0] # executive, legislative, judicial skill of the character - TODO could be a tuple
      self.aptitude = 0 # sum of all the skills
      self.conviction = 0 # conviction / stagnation of the character's politics
      self.ageMod = 0
    
      self.genProfile()

  def genProfileInput(self):
    # get player name
    self.setNameInput()
    
    # check if player wants to use a historical character
    for character in historicalCharacters:
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
  
  def printProfile(self):
    #print(name + " is a " + profile[0] + " from " + profile[1] + " representing the " + profile[2] + " Party.")
    pass
  
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

'''skills:
  each experience will have skills that they benefit:
  several levels for each experience level, locked by education
  executive:
    small business owner
    CEO
    
    mayor
    governor
    
    military serviceman
    military general
    
    vice president
    past president
    
  legislative:
    business decision board
    county board
    
    state senator
    congressman
    
  judicial:
    police officer
    police chief
    
    lawyer
    prosecutor
    
    local judge
    federal judge
    
    party leader
    speaker of the house
    
  player generation will be a series of selection or minigames
  players will have a set amount of points to distribute into each category
  name costs no points
  age costs points following -(((x-55)^2)/20)+100
  origin costs points according to state population
  education costs points according to (12(x-1))
  alignment does not cost???
  experience is tbd
  skills are generated - no point cost
  aptitude is based on skills - no point cost
  conviction is generated - no point cost
'''

namesM = [
  # the 50 most common male first names in the US
  "James",
  "Robert",
  "John",
  "Michael",
  "David",
  "William",
  "Richard",
  "Joseph",
  "Thomas",
  "Christopher",
  "Charles",
  "Daniel",
  "Matthew",
  "Anthony",
  "Mark",
  "Donald",
  "Steven",
  "Andrew",
  "Paul",
  "Joshua",
  "Kenneth",
  "Kevin",
  "Brian",
  "George",
  "Timothy",
  "Ronald",
  "Jason",
  "Edward",
  "Jeffrey",
  "Ryan",
  "Jacob",
  "Gary",
  "Nicholas",
  "Eric",
  "Johnathan",
  "Stephen",
  "Larry",
  "Justin",
  "Scott",
  "Brandon",
  "Benjamin",
  "Samuel",
  "Gregory",
  "Alexander",
  "Patrick",
  "Frank",
  "Raymond",
  "Jack",
  "Dennis",
  "Jerry"
]

namesF = [
  # the 50 most common female first names in the US
  "Mary",
  "Patricia",
  "Jennifer",
  "Linda",
  "Elizabeth",
  "Barbara",
  "Susan",
  "Jessica",
  "Sarah",
  "Karen",
  "Lisa",
  "Nancy",
  "Beatrice",
  "Sandra",
  "Margaret",
  "Ashley",
  "Kimberly",
  "Emily",
  "Donna",
  "Michelle",
  "Carol",
  "Amanda",
  "Melissa",
  "Deborah",
  "Stephanie",
  "Dorothy",
  "Rebecca",
  "Sharon",
  "Laura",
  "Cynthia",
  "Amy",
  "Kathleen",
  "Angela",
  "Shirley",
  "Brenda",
  "Emma",
  "Anna",
  "Pamela",
  "Nicole",
  "Samantha",
  "Katherine",
  "Christine",
  "Helen",
  "Debra",
  "Rachel",
  "Carolyn",
  "Janet",
  "Maria",
  "Catherine",
  "Heather"
  ]
  
surnames = [
  # the 100 most common last names in the US
  "SMITH",
  "JOHNSON",
  "WILLIAMS",
  "BROWN",
  "JONES",
  "GARCIA",
  "MILLER",
  "DAVIS",
  "RODRIGUEZ",
  "MARTINEZ",
  "HERNANDEZ",
  "LOPEZ",
  "GONZALES",
  "WILSON",
  "ANDERSON",
  "THOMAS",
  "TAYLOR",
  "MOORE",
  "JACKSON",
  "MARTIN",
  "LEE",
  "PEREZ",
  "THOMPSON",
  "WHITE",
  "HARRIS",
  "SANCHEZ",
  "CLARK",
  "RAMIREZ",
  "LEWIS",
  "ROBINSON",
  "WALKER",
  "YOUNG"
  "ALLEN",
  "KING",
  "WRIGHT",
  "SCOTT",
  "TORRES",
  "NGUYEN",
  "HILL",
  "FLORES",
  "GREEN",
  "ADAMS",
  "NELSON",
  "BAKER",
  "HALL",
  "RIVERA",
  "CAMPBELL",
  "MITCHELL",
  "CARTER",
  "ROBERTS",
  "GOMEZ",
  "PHILLIPS",
  "EVANS",
  "TURNER",
  "DIAZ",
  "PARKER",
  "CRUZ",
  "EDWARDS",
  "COLLINS",
  "REYES",
  "STEWART",
  "MORRIS",
  "MORALES",
  "MURPHY",
  "COOK",
  "ROGERS",
  "GUTIERREZ",
  "ORITZ",
  "MORGAN",
  "COOPER",
  "PETERSON",
  "BAILEY",
  "REED",
  "KELLY",
  "HOWARD",
  "RAMOS",
  "KIM",
  "COX",
  "WARD",
  "RICHARDSON",
  "WATSON",
  "BROOKS",
  "CHAVEZ",
  "WOOD",
  "JAMES",
  "BENNET",
  "GRAY",
  "MENDOZA",
  "RUIZ",
  "HUGHES",
  "PRICE",
  "ALVAREZ",
  "CASTILLO",
  "SANDERS",
  "PATEL",
  "MYERS",
  "LONG",
  "ROSS",
  "FOSTER",
  "JIMENEZ"
  ]
  
historicalCharacters = [
  # will be a list of buildstrings
  ["George WASHINGTON",57,"Virginia",3,[10,10],95],
  ["Thomas JEFFERSON",57,"Virginia"],
  ["Abraham LINCOLN",52,"Illinois",4,[],80],
  ["Theodore ROOSEVELT",42,"New York",6,[],75],
  ["Woodrow WILSON",56,"New Jersey",8,[],50],
  ["Franklin D ROOSEVELT",51,"New York",6,[],65],
  ["John F KENNEDY",43,"Massachusetts",6,[],60]
  ]

def sLen(value,length): # standardize length
  value = str(value)
  while len(value) < length:
    value = "0" + value
  return value