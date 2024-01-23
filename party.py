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
    
    
democrats = [
  "Democratic Party",
  "Democrats",
  "D",
  "Democrat",
  "Democratic National Comittee"
  [-100,-100],
  [],
  None, # chairperson character
  None, # candidate character
  None # candidate character
  ]
  
republicans = [
  "Republican Party",
  "Republicans",
  "R",
  "Republican",
  "Republican National Comittee",
  [100,100],
  [],
  None, # chairperson character
  None, # candidate character
  None # candidate character
  ]