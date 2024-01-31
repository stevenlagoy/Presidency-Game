'''primaries:
  attributes:
  - open / closed
  - primary / caucus

'''

class Convention:
  instances = []
  def __init__(self, name, ptype, delegates, fee, bonus, cards, string = None):
    self.__class__.instances.append(self)
    
    if string is not None:
      pass
    else:
      self.name = name
      self.ptype = ptype # 'primary' or 'caucus'
      self.delegates = delegates
      self.fee = fee
      self.bonus = bonus
      self.cards = cards
      
    def __repr__(self):
      return (
        "PR" +
        "-NA" + str(self.name) +
        "-PT" + str(self.ptype) +
        "-DE" + str(self.delegates) +
        "-FE" + str(self.fee) +
        "-BO" + str(self.bonus) +
        "-CA" + str(self.cards)
        )

primaries = [
  ["Iowa","P",10,10,20,1],
  ["New Hampshire","P",10,10,20,1],
  ["Southern","P",285,40,60,3],
  ["Midwest","P",85,30,40,2],
  ["Northeast","P",260,35,50,2],
  ["West","P",285,40,60,3],
  ["New England","P",65,25,30,1]
  ]

Primary.instances = []
for primary_ in primaries:
  Primary(primary_[0],primary_[1],primary_[2],primary_[3],primary_[4],primary_[5])

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
    "location": "Northeast",
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
  },
  {

  }
]