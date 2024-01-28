class Party:
  instances = []
  def __init__(self):
    self.__class__.instances.append(self)
    self.longname = ""
    self.shortname = ""
    self.abbreviation = ""
    self.demonym = ""
    self.comittee = ""
    self.alignment = [0,0]
    self.members = []
    self.chairperson = None
    self.p_candidate = None
    self.vp_candidate = None
    

parties = [
  {
    "name" : "Democratic Party",
    "adjective" : "Democrats",
    "abbreviation" : "D",
    "governing body" : "Democratic National Committee",
    "color" : "blue",
    "symbol" : "donkey",
    "icon" : "democrat_logo.png"
  },
  {
    "name" : "Republican Party",
    "adjective" : "Republican",
    "abbreviation" : "R",
    "governing body" : "Republican National Committee",
    "color" : "red",
    "symbol" : "elephant",
    "icon" : "republican_logo.png"
  },
  {
    "name" : "Progressive Party",
    "adjective" : "Bull Moose",
    "abbreviation" : "P",
    "governing body" : "",
    "color" : "red",
    "symbol" : "bull moose",
    "icon" : "progressive_logo.png"
  },
  {
    "name" : "Democratic-Republican Party",
    "adjective" : "Democratic-Republican",
    "abbreviation" : "D-R",
    "governing body" : "",
    "color" : "red, white, blue",
    "symbol" : "",
    "icon" : "democratic_republican_cockade.png"
  },
  {
    "name" : "Federalist Party",
    "adjective" : "Federalist",
    "abbreviation" : "F",
    "governing body" : "",
    "color" : "black, white",
    "symbol" : "",
    "icon" : "federalist_cockade.png"
  },
  {
    "name" : "Green Party",
    "adjective" : "Green",
    "abbreviation" : "G",
    "governing body" : "Green National Committee",
    "color" : "green",
    "symbol" : "Sunflower",
    "icon" : "green_logo.png"
  },
  {
    "name" : "Liberatarian Party",
    "adjective" : "Liberatarian",
    "abbreviation" : "L",
    "governing body" : "Liberatarian National Committee",
    "color" : "yellow",
    "symbol" : "hedgehog", # the official symbol is the Statue of Liberty - the hedgehog is used unofficially by many party members
    "icon" : "democratic_republican_cockade.png"
  },
  {
    "name" : "Whig Party",
    "adjective" : "Whig",
    "abbreviation" : "W",
    "governing body" : "",
    "color" : "blue, buff",
    "symbol" : "owl", # the owl is only used contemporarily by the Modern Whig Institute
    "icon" : "whig_logo.png"
  },
  {
    "name" : "People's Party",
    "adjective" : "Populist",
    "abbreviation" : "P",
    "governing body" : "",
    "color" : "red",
    "symbol" : "",
    "icon" : "populist_logo.png"
  },
  {
    "name" : "Socialist Party",
    "adjective" : "Socialist",
    "abbreviation" : "S",
    "governing body" : "",
    "color" : "red",
    "symbol" : "",
    "icon" : "socialist_logo.png"
  },
  {
    "name" : "Centrist",
    "adjective" : "Centrist",
    "abbreviation" : "", # no abbreviation
    "governing body" : "", # no governing body
    "color" : "gray",
    "symbol" : "balances",
    "icon" : "centrist_icon.png"
  }
]