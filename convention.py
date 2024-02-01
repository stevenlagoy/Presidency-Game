'''primaries:
  attributes:
  - open / closed
  - primary / caucus

'''

class Convention:
  instances = []
  def __init__(self):
    self.__class__.instances.append(self)
      
  def __repr__(self):
    return None

class Primary(Convention):
  instances = []
  def __init__(self, type):
    self.__class__.instances.append(self)
    self.type = type # open or closed
    # closed primaries are open only to registered party members
    # semi-open primaries are open to anyone who is not a member of the other party ("cross-voting")
    # open primaries are open to all voters

class Caucus(Convention):
  instances = []
  def __init__(self):
    self.__class__.instances.append(self)

'''
there are primaries or caucuses in all 50 states, DC, and 5 territories for the republicans, and the same plus "Democrats Abroad" for the democrats
samoa, dems abroad, northern marianas, puerto rico, DC, guam, virgin islands
Virgin islands, DC, guam, Northern marianas, samoa, puerto rico
'''

republican_primaries = [
  {
    "state": "Iowa",
    "location": "Midwest",
    "delegates": 40,
    "date": "February 3, 2024"
  },
  {
    "state": "New Hampshire",
    "location": "East",
    "delegates": 22,
    "date": "February 11, 2024"
  },
  {
    "state": "South Carolina",
    "location": "South",
    "delegates": 50,
    "date": "February 20, 2024"
  },
  {
    "state": "Nevada",
    "location": "West",
    "delegates": 30,
    "date": "February 22, 2024"
  }
]

'''
dates:
  states often attempt to "front-load" their primaries: have them as early as possible, to make their delegates have as much influence as possible
  the parties, in turn, attempt to spread the dates out over 90 days by offering penalty or bonus delegates, though this can make the minority party suffer penalties to delegate numbers

REPUBLICANS
  https://prod-static.gop.com/media/Rules_of_the_Republican_Party_090921.pdf
  The Republican Party Rule 16(c)(1) prohibits states from holding a primary/caucus before March 1 (except for iowa, new hampshire, nevada, and south carolina)
  The excepted states from 16c1 can have their processes no earlier than one month before the next earliest state
  all primaries/caucuses occur before the second saturday in June OR less than 45 days before the national convention
  all primaries/caucuses before march 15 are always proportional based on number of statewide votes and number of votes for each candidate, though states can choose a threshold that a candidate must pass (if its <20%) to recieve any votes, AND can choose a threshold (>50%) after which ALL delegates go to one candidate 

  META 
  should the dates and delegates be generated for each game, or kept constant? need to figure out what parties are in power in each state too

  DEMOCRATS
    https://democrats.org/wp-content/uploads/2022/02/DNC-Charter-Bylaws-10.09.2021.pdf
    This party doesn't seem to have any publically-available rules for when primaries or caucuses can be held....
    new hampshire is traditionally the first primary. in 2024, they violated the democratic party's ordering preferences and were stripped of all 23 delegates as a result
  
  Alabama
  Alaska        
  Arizona       
  American Samoa
  Arkansas      
  California    
  Colorado
  Connecticut
  Delaware
  District of Columbia
  Florida
  Georgia
  Guam
  Hawaii
  Idaho
  Illinois
  Indiana
  Iowa
  Kansas
  Kentucky
  Louisiana
  Maine
  Maryland
  Massachusetts
  Michigan
  Minnesota
  Mississippi
  Missouri
  Montana
  Nebraska
  Nevada
  New Hampshire - Second tuesday in March OR 7 days or more before the earliest other primary
  New Jersey
  New Mexico
  New York
  North Carolina
  North Dakota
  Northern Mariana Islands
  Ohio
  Oklahoma
  Oregon
  Pennsylvania
  Puerto Rico
  Rhode Island
  South Carolina
  South Dakota
  Tennessee
  Texas
  Utah
  Vermont
  Virginia
  U.S. Virgin Islands
  Washington
  West Virginia
  Wisconsin
  Wyoming

primaries:
  closed - only voters registered with a given party may vote
    connecticut
    delaware
    florida
    kansas
    kentucky
    louisiana
    maryland
    nebraska
    nevada
    new mexico
    new york
    pennsylvania
    wyoming
    washington DC
  semi-closed - unaffiliated voters may vote in only one of the primaries
    alaska
    arizona
    california
    colorado
    illinois
    iowa
    kansas
    maine
    new hampshire
    new jersey
    north carolina
    ohio
    oregon
    rhode island
    utah
    west virginia
    massachusetts - members of minor parties may change their registration more than 20 days before the primary
  partially open - allows voters to cross lines, but they must publically declare their ballot choice

  open - any citizen may vote in only one of the primaries
    alabama
    arkansas
    georgia
    hawaii
    michigan
    minnesota
    missouri
    montana
    north dakota
    south carolina
    texas
    vermont
    virginia
    wisconsin
    puerto rico
  blanket - also called top-two, voters list their party, but may vote for anyone listed on the common (two parties in one) ballot. the top two advance to general election
    louisiana
    california
    washington
  other
    
'''