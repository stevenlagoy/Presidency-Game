class State:
  instances = [] # list of pointers to all class instances
  def __init__(self, name, population, largest_cities, abbreviation, universities, string = None):
    self.__class__.instances.append(self) # add self-pointer to list of instances of class
    if string is not None:
      attributes = string.split("-")
      tags = [tag[:2] for tag in attributes]
      self.name = attributes[tags.index("NA")][2:]
      self.abb = attributes[tags.index("AB")][2:]
      self.population = int(attributes[tags.index("PO")][2:])

    else:
      self.name = name
      self.population = population
      self.largest_cities = largest_cities
      self.capital = largest_cities[0]
      self.abbreviation = abbreviation
      self.universities = universities
    
  def __repr__(self):
        return (
            f"State(name={repr(self.name)}, "
            f"population={self.population}, "
            f"largest_cities={repr(self.largest_cities)}, "
            f"abbreviation={repr(self.abbreviation)}, "
            f"universities={repr(self.universities)})"
        )


# how to dynamically change populations... could come up with a function of some kind and have users select a year
# or just set them to a fixed value - 2020 Census

'''
states = [
  "ST-NAAlabama-ABAL-PO5024279",
  "ST-NAAlaska-ABAK-PO733391",
  "ST-NAArizona-ABAZ-PO7151502",
  "ST-NAArkansas-ABAK-PO3011524",
  "ST-NAAmerican Samoa-ABAS-PO46189",
  "ST-NACalifornia-ABCA-PO39538223",
  "ST-NAColorado-ABCO-PO5773714",
  "ST-NAConnecticut-ABCT-PO3605944",
  "ST-NADelaware-ABDE-PO989948",
  "ST-NADistrict Of Columbia-ABDC-PO689545",
  "ST-NAFlorida-ABFL-PO21538187",
  "ST-NAGeorgia-ABGA-PO10711908",
  "ST-NAGuam-ABGU-PO169231",
  "ST-NAHawaii-ABHI-PO1455271", 
  "ST-NAIdaho-ABID-PO1839106",
  "ST-NAIllinois-ABIL-PO12812508",
  "ST-NAIndiana-ABIN-PO6785528",
  "ST-NAIowa-ABIA-PO3190369",
  "ST-NAKansas-ABKS-PO2937880",
  "ST-NAKentucky-ABKY-PO4505836",
  "ST-NALouisiana-ABLA-PO4657757",
  "ST-NAMaine-ABME-PO1362359",
  "ST-NAMaryland-ABMD-PO6177331",
  "ST-NAMassachusetts-ABMA-PO7029917",
  "ST-NAMichigan-ABMI-PO10077331",
  "ST-NAMinnesota-ABMN-PO5706494",
  "ST-NAMississippi-ABMS-PO2961279",
  "ST-NAMissouri-ABMO-PO6154913",
  "ST-NAMontana-ABMT-PO1084225",
  "ST-NANebraska-ABNE-PO1961504",
  "ST-NANevada-ABNV-PO3104614",
  "ST-NANew Hampshire-ABNH-PO1377529",
  "ST-NANew Jersey-ABNJ-PO9288994",
  "ST-NANew Mexico-ABNM-PO2117522",
  "ST-NANew York-ABNY-PO20201249",
  "ST-NANorth Carolina-ABNC-PO10439388",
  "ST-NANorth Dakota-ABND-PO779094",
  "ST-NANorthern Mariana Islands-ABMP-PO49587",
  "ST-NAOhio-ABOH-PO11799448", 
  "ST-NAOklahoma-ABOK-PO3959353",
  "ST-NAOregon-ABOR-PO4237256",
  "ST-NAPennsylvania-ABPA-PO13002700",
  "ST-NAPuerto Rico-ABPR-PO3271564",
  "ST-NARhode Island-ABRI-PO1097379",
  "ST-NASouth Carolina-ABSC-PO5118425",
  "ST-NASouth Dakota-ABSD-PO886667",
  "ST-NATennessee-ABTN-PO6910840",
  "ST-NATexas-ABTX-PO29145505",
  "ST-NAUtah-ABUT-PO3271616",
  "ST-NAVermont-ABVT-PO642077",
  "ST-NAVirginia-ABVA-PO8631393",
  "ST-NAVirgin Islands-ABVI-PO87146",
  "ST-NAWashington-ABWA-PO7705281",
  "ST-NAWest Virginia-ABWV-PO1793716",
  "ST-NAWisconsin-ABWI-PO5893718",
  "ST-NAWyoming-ABWY-PO576851"
]
'''
states = [ # note: this list was generated with the help of AI. There may be innaccuracies.
  {
    "name" : "Alabama",
    "population" : 5024279,
    "largest_cities" : ["Montgomery", "Huntsville", "Birmingham", "Mobile", "Tuscaloosa"],
    "abbreviation" : "AL",
    "universities" : ["University of Alabama", "Auburn University", "University of Alabama at Birmingham", "Columbia Southern University"]
  },
  {
    "name" : "Alaska",
    "population" : 733391,
    "largest_cities" : ["Juneau", "Anchorage", "Fairbanks", "Wasilla", "Sitka"],
    "abbreviation" : "AL",
    "universities" : ["University of Alaska"]
  },
  {
    "name": "Arizona",
    "population": 7378494,
    "largest_cities": ["Phoenix", "Tucson", "Mesa", "Chandler", "Glendale"],
    "abbreviation": "AZ",
    "universities": ["University of Arizona", "Arizona State University", "University of Phoenix"]
  },
  {
    "name": "American Samoa",
    "population": 55197,
    "largest_cities": ["Pago Pago", "Tafuna", "Nu'uuli", "Ili'ili", "Aua"],
    "abbreviation": "AS",
    "universities": ["American Samoa Community College"]
  },
  {
    "name": "Arkansas",
    "population": 3038999,
    "largest_cities": ["Little Rock", "Fort Smith", "Fayetteville", "Springdale", "Jonesboro"],
    "abbreviation": "AR",
    "universities": ["University of Arkansas", "Arkansas State University"]
  },
  {
    "name": "California",
    "population": 39538223,
    "largest_cities": ["Sacramento", "Los Angeles", "San Diego", "San Jose", "San Francisco", "Fresno"],
    "abbreviation": "CA",
    "universities": ["University of California, Berkeley", "Stanford University", "University of Southern California"]
  },
  {
    "name": "Colorado",
    "population": 5773714,
    "largest_cities": ["Denver", "Colorado Springs", "Aurora", "Fort Collins", "Lakewood"],
    "abbreviation": "CO",
    "universities": ["University of Colorado Boulder", "Colorado State University", "University of Denver"]
  },
  {
    "name": "Connecticut",
    "population": 3565287,
    "largest_cities": ["Hartford", "Bridgeport", "New Haven", "Stamford", "Waterbury"],
    "abbreviation": "CT",
    "universities": ["Yale University", "University of Connecticut", "Quinnipiac University"]
  },
  {
    "name": "Delaware",
    "population": 982895,
    "largest_cities": ["Dover", "Wilmington", "Newark", "Middletown", "Smyrna"],
    "abbreviation": "DE",
    "universities": ["University of Delaware", "Delaware State University"]
  },
  {
    "name": "District of Columbia",
    "population": 689545,
    "largest_cities": ["Washington, D.C."],
    "abbreviation": "DC",
    "universities": ["Georgetown University", "George Washington University", "Howard University"]
  },
  {
    "name": "Florida",
    "population": 21477737,
    "largest_cities": ["Tallahassee", "Jacksonville", "Miami", "Tampa", "Orlando", "St. Petersburg"],
    "abbreviation": "FL",
    "universities": ["University of Florida", "University of Central Florida", "Florida State University", "University of Miami", "Florida International University"]
  },
  {
    "name": "Georgia",
    "population": 10736059,
    "largest_cities": ["Atlanta", "Augusta", "Columbus", "Macon", "Savannah"],
    "abbreviation": "GA",
    "universities": ["University of Georgia", "Georgia Institute of Technology", "Emory University"]
  },
  {
    "name": "Guam",
    "population": 168775,
    "largest_cities": ["Hagåtña", "Dededo", "Yigo", "Tamuning", "Mangilao", "Barrigada"],
    "abbreviation": "GU",
    "universities": ["University of Guam"]
  },
  {
    "name": "Hawaii",
    "population": 1455271,
    "largest_cities": ["Honolulu", "Pearl City", "Hilo", "Kailua", "Waipahu"],
    "abbreviation": "HI",
    "universities": ["University of Hawaii at Manoa", "Brigham Young University–Hawaii"]
  },
  {
    "name": "Idaho",
    "population": 1826156,
    "largest_cities": ["Boise", "Meridian", "Nampa", "Idaho Falls", "Pocatello"],
    "abbreviation": "ID",
    "universities": ["Boise State University", "University of Idaho", "Idaho State University"]
  },
  {
    "name": "Illinois",
    "population": 12659682,
    "largest_cities": ["Springfield", "Chicago", "Aurora", "Rockford", "Joliet", "Naperville"],
    "abbreviation": "IL",
    "universities": ["University of Illinois Urbana-Champaign", "Northwestern University", "University of Chicago"]
  },
  {
    "name": "Indiana",
    "population": 6732219,
    "largest_cities": ["Indianapolis", "Fort Wayne", "Evansville", "South Bend", "Carmel"],
    "abbreviation": "IN",
    "universities": ["Indiana University Bloomington", "Purdue University", "University of Notre Dame"]
  },
  {
    "name": "Iowa",
    "population": 3163561,
    "largest_cities": ["Des Moines", "Cedar Rapids", "Davenport", "Sioux City", "Iowa City"],
    "abbreviation": "IA",
    "universities": ["University of Iowa", "Iowa State University", "University of Northern Iowa", "Ashford University", "Kaplan University"]
  },
  {
    "name": "Kansas",
    "population": 2913314,
    "largest_cities": ["Topeka", "Wichita", "Overland Park", "Kansas City", "Olathe"],
    "abbreviation": "KS",
    "universities": ["University of Kansas", "Kansas State University", "Wichita State University"]
  },
  {
    "name": "Kentucky",
    "population": 4499692,
    "largest_cities": ["Frankfort", "Louisville", "Lexington", "Bowling Green", "Owensboro", "Covington"],
    "abbreviation": "KY",
    "universities": ["University of Kentucky", "University of Louisville", "Western Kentucky University"]
  },
  {
    "name": "Louisiana",
    "population": 4645184,
    "largest_cities": ["Baton Rouge", "New Orleans", "Shreveport", "Lafayette", "Lake Charles"],
    "abbreviation": "LA",
    "universities": ["Louisiana State University", "Tulane University", "University of Louisiana at Lafayette"]
  },
  {
    "name": "Maine",
    "population": 1362359,
    "largest_cities": ["Agusta", "Portland", "Lewiston", "Bangor", "South Portland", "Auburn"],
    "abbreviation": "ME",
    "universities": ["University of Maine", "Bates College", "Bowdoin College"]
  },
  {
    "name": "Maryland",
    "population": 6045680,
    "largest_cities": ["Annapolis", "Baltimore", "Columbia", "Germantown", "Silver Spring", "Frederick"],
    "abbreviation": "MD",
    "universities": ["University of Maryland, College Park", "Johns Hopkins University", "University of Maryland, Baltimore County"]
  },
  {
    "name": "Massachusetts",
    "population": 6892503,
    "largest_cities": ["Boston", "Worcester", "Springfield", "Cambridge", "Lowell"],
    "abbreviation": "MA",
    "universities": ["Harvard University", "Massachusetts Institute of Technology (MIT)", "Boston University"]
  },
  {
    "name": "Michigan",
    "population": 10045029,
    "largest_cities": ["Lansing", "Detroit", "Grand Rapids", "Warren", "Sterling Heights", "Ann Arbor"],
    "abbreviation": "MI",
    "universities": ["University of Michigan, Ann Arbor", "Michigan State University", "Wayne State University"]
  },
  {
    "name": "Minnesota",
    "population": 5709752,
    "largest_cities": ["Saint Paul", "Minneapolis", "Rochester", "Duluth", "Bloomington"],
    "abbreviation": "MN",
    "universities": ["University of Minnesota, Twin Cities", "Minnesota State University, Mankato", "St. Olaf College"]
  },
  {
    "name": "Mississippi",
    "population": 2961279,
    "largest_cities": ["Jackson", "Gulfport", "Southaven", "Hattiesburg", "Biloxi"],
    "abbreviation": "MS",
    "universities": ["University of Mississippi (Ole Miss)", "Mississippi State University", "Jackson State University"]
  },
  {
    "name": "Missouri",
    "population": 6151548,
    "largest_cities": ["Jefferson City", "Kansas City", "St. Louis", "Springfield", "Columbia", "Independence"],
    "abbreviation": "MO",
    "universities": ["University of Missouri, Columbia", "Washington University in St. Louis", "Missouri State University"]
  },
  {
    "name": "Montana",
    "population": 1086759,
    "largest_cities": ["Helena", "Billings", "Missoula", "Great Falls", "Bozeman", "Butte"],
    "abbreviation": "MT",
    "universities": ["University of Montana", "Montana State University", "Montana Tech"]
  },
  {
    "name": "Nebraska",
    "population": 1961504,
    "largest_cities": ["Lincoln", "Omaha", "Bellevue", "Grand Island", "Kearney"],
    "abbreviation": "NE",
    "universities": ["University of Nebraska-Lincoln", "Creighton University", "University of Nebraska Omaha"]
  },
  {
    "name": "Nevada",
    "population": 3108462,
    "largest_cities": ["Carson City", "Las Vegas", "Henderson", "Reno", "North Las Vegas", "Sparks"],
    "abbreviation": "NV",
    "universities": ["University of Nevada, Las Vegas (UNLV)", "University of Nevada, Reno", "Nevada State College"]
  },
  {
    "name": "New Hampshire",
    "population": 1371246,
    "largest_cities": ["Concord", "Manchester", "Nashua", "Derry", "Dover"],
    "abbreviation": "NH",
    "universities": ["Dartmouth College", "University of New Hampshire", "Southern New Hampshire University"]
  },
  {
    "name": "New Jersey",
    "population": 9288994,
    "largest_cities": ["Trenton", "Newark", "Jersey City", "Paterson", "Elizabeth", "Edison"],
    "abbreviation": "NJ",
    "universities": ["Princeton University", "Rutgers University", "Stevens Institute of Technology"]
  },
  {
    "name": "New Mexico",
    "population": 2117522,
    "largest_cities": ["Santa Fe", "Albuquerque", "Las Cruces", "Rio Rancho", "Roswell"],
    "abbreviation": "NM",
    "universities": ["University of New Mexico", "New Mexico State University", "New Mexico Institute of Mining and Technology"]
  },
  {
    "name": "New York",
    "population": 19453561,
    "largest_cities": ["Albany", "New York City", "Buffalo", "Rochester", "Yonkers", "Syracuse"],
    "abbreviation": "NY",
    "universities": ["Columbia University", "New York University (NYU)", "Cornell University"]
  },
  {
    "name": "North Carolina",
    "population": 10611862,
    "largest_cities": ["Raleigh", "Charlotte", "Greensboro", "Durham", "Winston-Salem"],
    "abbreviation": "NC",
    "universities": ["University of North Carolina at Chapel Hill", "Duke University", "North Carolina State University"]
  },
  {
    "name": "North Dakota",
    "population": 761723,
    "largest_cities": ["Bismarck", "Fargo", "Grand Forks", "Minot", "West Fargo"],
    "abbreviation": "ND",
    "universities": ["University of North Dakota", "North Dakota State University", "Minot State University"]
  },
  {
    "name": "Northern Mariana Islands",
    "population": 56882,
    "largest_cities": ["Saipan", "Tinian", "Rota"],
    "abbreviation": "MP",
    "universities": ["Northern Marianas College"]
  },
  {
    "name": "Ohio",
    "population": 11799448,
    "largest_cities": ["Columbus", "Cleveland", "Cincinnati", "Toledo", "Akron"],
    "abbreviation": "OH",
    "universities": ["Ohio State University", "University of Cincinnati", "Case Western Reserve University"]
  },
  {
    "name": "Oklahoma",
    "population": 3959353,
    "largest_cities": ["Oklahoma City", "Tulsa", "Norman", "Broken Arrow", "Lawton"],
    "abbreviation": "OK",
    "universities": ["University of Oklahoma", "Oklahoma State University", "University of Tulsa"]
  },
  {
    "name": "Oregon",
    "population": 4217737,
    "largest_cities": ["Salem", "Portland", "Eugene", "Gresham", "Hillsboro"],
    "abbreviation": "OR",
    "universities": ["University of Oregon", "Oregon State University", "Portland State University"]
  },
  {
    "name": "Pennsylvania",
    "population": 12820878,
    "largest_cities": ["Harrisburg", "Philadelphia", "Pittsburgh", "Allentown", "Erie", "Reading"],
    "abbreviation": "PA",
    "universities": ["University of Pennsylvania", "Penn State University", "Carnegie Mellon University"]
  },
  {
    "name": "Puerto Rico",
    "population": 3193694,
    "largest_cities": ["San Juan", "Bayamón", "Carolina", "Ponce", "Caguas"],
    "abbreviation": "PR",
    "universities": ["University of Puerto Rico", "Sistema Universitario Ana G. Mendez", "Inter American University of Puerto Rico"]
  },
  {
    "name": "Rhode Island",
    "population": 1097379,
    "largest_cities": ["Providence", "Warwick", "Cranston", "Pawtucket", "East Providence"],
    "abbreviation": "RI",
    "universities": ["Brown University", "University of Rhode Island", "Providence College"]
  },
  {
    "name": "South Carolina",
    "population": 5148714,
    "largest_cities": ["Columbia", "Charleston", "North Charleston", "Mount Pleasant", "Rock Hill"],
    "abbreviation": "SC",
    "universities": ["University of South Carolina", "Clemson University", "College of Charleston"]
  },
  {
    "name": "South Dakota",
    "population": 884659,
    "largest_cities": ["Pierre", "Sioux Falls", "Rapid City", "Aberdeen", "Brookings", "Watertown"],
    "abbreviation": "SD",
    "universities": ["University of South Dakota", "South Dakota State University", "Dakota State University"]
  },
  {
    "name": "Tennessee",
    "population": 6910840,
    "largest_cities": ["Nashville", "Memphis", "Knoxville", "Chattanooga", "Clarksville"],
    "abbreviation": "TN",
    "universities": ["Vanderbilt University", "University of Tennessee, Knoxville", "Belmont University"]
  },
  {
    "name": "Texas",
    "population": 29145505,
    "largest_cities": ["Austin", "Houston", "San Antonio", "Dallas", "Fort Worth"],
    "abbreviation": "TX",
    "universities": ["University of Texas at Austin", "Texas A&M University", "Rice University", "Dallas College"]
  },
  {
    "name": "Utah",
    "population": 3271616,
    "largest_cities": ["Salt Lake City", "West Valley City", "Provo", "West Jordan", "Orem"],
    "abbreviation": "UT",
    "universities": ["University of Utah", "Brigham Young University", "Utah State University"]
  },
  {
    "name": "Vermont",
    "population": 643077,
    "largest_cities": ["Montpelier", "Burlington", "South Burlington", "Rutland", "Essex Junction", "Barre"],
    "abbreviation": "VT",
    "universities": ["University of Vermont", "Middlebury College", "Norwich University"]
  },
  {
    "name": "Virginia",
    "population": 8626207,
    "largest_cities": ["Richmond", "Virginia Beach", "Norfolk", "Chesapeake", "Arlington"],
    "abbreviation": "VA",
    "universities": ["University of Virginia", "Virginia Tech", "George Mason University", "Liberty University"]
  },
  {
    "name": "U.S. Virgin Islands",
    "population": 106977,
    "largest_cities": ["Charlotte Amalie", "Anna's Retreat", "St. Croix", "Frederiksted", "Christiansted"],
    "abbreviation": "VI",
    "universities": ["University of the Virgin Islands"]
  },
  {
    "name": "Washington",
    "population": 7693612,
    "largest_cities": ["Olympia", "Seattle", "Spokane", "Tacoma", "Vancouver", "Bellevue"],
    "abbreviation": "WA",
    "universities": ["University of Washington", "Washington State University", "Seattle University"]
  },
  {
    "name": "West Virginia",
    "population": 1778070,
    "largest_cities": ["Charleston", "Huntington", "Parkersburg", "Morgantown", "Wheeling"],
    "abbreviation": "WV",
    "universities": ["West Virginia University", "Marshall University", "West Liberty University"]
  },
  {
    "name": "Wisconsin",
    "population": 5851754,
    "largest_cities": ["Madison", "Milwaukee", "Green Bay", "Kenosha", "Racine"],
    "abbreviation": "WI",
    "universities": ["University of Wisconsin-Madison", "Marquette University", "University of Wisconsin-Milwaukee"]
  },
  {
    "name": "Wyoming",
    "population": 576851,
    "largest_cities": ["Cheyenne", "Casper", "Laramie", "Gillette", "Rock Springs"],
    "abbreviation": "WY",
    "universities": ["University of Wyoming", "Casper College", "Wyoming Technical Institute"]
  }
]

State.instances = []
for state in states:
  State(state.get("name"), state.get("population"), state.get("largest_cities"), state.get("abbreviation"), state.get("universities"))

print([state.__repr__ for state in State.instances])
print(len(State.instances))