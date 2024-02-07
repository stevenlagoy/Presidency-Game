class Party:
    instances = []
    def __init__(self, name, adjective, abbreviation, governing_body, color_code, symbol, icon_address):
        self.__class__.instances.append(self)
        self.name = name
        self.adjective = adjective
        self.abbreviation = abbreviation
        self.governing_body = governing_body
        self.color_code = color_code
        self.symbol = symbol
        self.icon_address = icon_address

        self.members = [] # list of characters which are a part of the party
        self.issues = [] # list of issues and the levels of support held by each party
        self.conventions = [] # list of primaries and caucuses held by the party
        

parties = [
    {
        "name" : "Democratic Party",
        "adjective" : "Democrats",
        "abbreviation" : "D",
        "governing body" : "Democratic National Committee",
        "color" : "#0044C9",
        "symbol" : "donkey",
        "icon" : "democrat_logo.png"
    },
    {
        "name" : "Republican Party",
        "adjective" : "Republican",
        "abbreviation" : "R",
        "governing body" : "Republican National Committee",
        "color" : "#E81B23",
        "symbol" : "elephant",
        "icon" : "republican_logo.png"
    },
    {
        "name" : "Progressive Party",
        "adjective" : "Bull Moose",
        "abbreviation" : "P",
        "governing body" : "",
        "color" : "#B1333B",
        "symbol" : "bull moose",
        "icon" : "progressive_logo.png"
    },
    {
        "name" : "Democratic-Republican Party",
        "adjective" : "Democratic-Republican",
        "abbreviation" : "D-R",
        "governing body" : "",
        "color" : "#FFFDFD",
        "symbol" : "",
        "icon" : "democratic_republican_cockade.png"
    },
    {
        "name" : "Federalist Party",
        "adjective" : "Federalist",
        "abbreviation" : "F",
        "governing body" : "",
        "color" : "#000000",
        "symbol" : "",
        "icon" : "federalist_cockade.png"
    },
    {
        "name" : "Green Party",
        "adjective" : "Green",
        "abbreviation" : "G",
        "governing body" : "Green National Committee",
        "color" : "#00A95C",
        "symbol" : "Sunflower",
        "icon" : "green_logo.png"
    },
    {
        "name" : "Liberatarian Party",
        "adjective" : "Liberatarian",
        "abbreviation" : "L",
        "governing body" : "Liberatarian National Committee",
        "color" : "#FED105",
        "symbol" : "hedgehog", # the official symbol is the Statue of Liberty - the hedgehog is used unofficially by many party members
        "icon" : "democratic_republican_cockade.png"
    },
    {
        "name" : "Whig Party",
        "adjective" : "Whig",
        "abbreviation" : "W",
        "governing body" : "",
        "color" : "#044E77",
        "symbol" : "owl", # the owl is only used contemporarily by the Modern Whig Institute
        "icon" : "whig_logo.png"
    },
    {
        "name" : "People's Party",
        "adjective" : "Populist",
        "abbreviation" : "P",
        "governing body" : "",
        "color" : "#FF0000",
        "symbol" : "",
        "icon" : "populist_logo.png"
    },
    {
        "name" : "Socialist Democratic Party", # there are several real parties, including: Social Democrats, USA; Socialist Party USA; and the Democratic Socialist Organizing Committee
        "adjective" : "Socialist",
        "abbreviation" : "S",
        "governing body" : "",
        "color" : "#ED1F24",
        "symbol" : "Fist with Rose",
        "icon" : "socialist_logo.png"
    },
    {
        "name" : "Centrist",
        "adjective" : "Centrist",
        "abbreviation" : "", # no abbreviation
        "governing body" : "", # no governing body
        "color" : "#3F3F3F",
        "symbol" : "balances",
        "icon" : "centrist_icon.png"
    }
]

for party in parties:
    Party(
        party.get("name"),
        party.get("adjective"),
        party.get("abbreviation"),
        party.get("governing body"),
        party.get("color"),
        party.get("symbol"),
        party.get("icon")
    )