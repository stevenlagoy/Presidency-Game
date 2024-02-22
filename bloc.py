class Bloc:
    global num_voters
    instances = []
    def __init__(self, name, percentage_of_voters):
        self.__class__.instances.append(self)
                
        self.name = name
        self.percentage_of_voters = round((percentage_of_voters / 100), 6)
        self.num_voting_members = int(round((self.percentage_of_voters * num_voters), 0))

num_voters = 258327312

blocs = {
    "Generation" : {
        # solely from "Population Distribution inthe United States in 2022, by generation" published to Statista by Veera Korhonen
        # generation alpha has been given the remainder of 100 minus the sum of all other cohorts
        "Greatest Generation" : 0.20,
        "Silent Generation" : 5.49,
        "Baby Boomers" : 20.58,
        "Generation X" : 19.61,
        "Millennial" : 21.67,
        "Generation Z" : 20.88,
        "Generation Alpha" : 11.57
    },
    "Family" : {
        # primarily from "Marital status of the United States population in 2022, by sex" published to Statista by Veera Korhonen, and secondarily from "Americans' Preference for Larger Families Highest Since 1971" published to Gallup by Megan Brenan
        # percentages have been distributed according to the bottommost applicable classification
        # the control for sex has been reverted
        "Single" : 16.02,
        "Married" : 23.84,
        "Widdowed" : 2.66,
        "Divorced" : 4.48,
        "At least one child" : 32,
        "Three or more children" : 21
    },
    "Religion" : {
        # primarily from the PRRI American Values Atlas and secondarily from a 2014 Pew Research Center report
        # numbers for "unaffiliated" are distributed according to relative proportion, and "nothing in particular" has been called "unaffiliated" below
        # numbers for "Hispanic Protestant" and "other non-white Protestant" have been distributed between "Evangelical" (76%) and "Mainline Protestant" (24%) as reported by the Pew Research Center in 2013
        "Evangelical" : 20.1,
        "Mainline Protestant" : 18.2,
        "Black Protestant" : 7.3,
        "Catholic" : 21.8,
        "Mormon" : 1.3,
        "Jehovah's Witness" : 0.5,
        "Orthodox Christian" : 0.5,
        "Unaffiliated" : 16.1,
        "Agnostic" : 4.1,
        "Atheist" : 3.1,
        "Jewish" : 1.4,
        "Muslim" : 0.8,
        "Buddhist" : 0.8,
        "Hundu" : 0.5,
        "Other Non-Christian" : 3.5
    },
    "Education" : {
        # primarily from a 2022 report by the Census Bureau
        # persons under 25 years of age (12% pop) COULD be distributed into the four earliest education brackets @ 72%, 8%, 16%, and 4% respectively
        "Primary Education" : 8.8,
        "Secondary Education" : 27.9,
        "Tertiary Education" : 14.9,
        "College Degree" : 34.0,
        "Higher Degree" : 14.4
    },
    "Race & Ethnicity" : {
        # primarily from the Census Bureau's 2020 Census and American Community Survey Report 2022
        # these numbers have been adjusted to distribute "two or more races" according to their relative proportions
        "European (White)" : 59.63,
        "African American (Black)" : 12.23,
        "Native American Indian" : 0.71,
        "East Asian" : 2.54,
        "South Asian" : 2.53,
        "Southeast Asian" : 3.16,
        "Pacific Islander" : 0.24,
        "Hispanic / Latino" : 18.96
    },
    "Employment" : {
        # primarily from "Number of employees in the United States in December 2023, by industry" published by Statista Research Department
        # student numbers from the 2022 Current Population Survey
        # employed attendees of any public or private school or college are distributed only as "Student", even if otherwise employed
        "Unemployed" : 3.7,
        "Student" : 29.1,
        "Professional & Business" : 8.8825,
        "Healthcare" : 8.4738,
        "Hospitality" : 6.4964,
        "Retail Trade" : 6.0169,
        "Local Government" : 5.6951,
        "Manufacturing" : 5.0270,
        "Construction" : 3.1185,
        "Finance & Insurance" : 2.5931,
        "Transportation & Warehousing" : 2.5712,
        "Wholesale Trade" : 2.3610,
        "Other Services" : 2.2820,
        "State Government" : 2.0617,
        "Private Education" : 1.5435,
        "Information" : 1.1811,
        "Federal Government" : 1.1478,
        "Real Estate" : 0.9480,
        "Resource Extraction" : 0.2481,
        "Utility" : 0.2175,
        "Other Sector" : 6.3348
    },
    "Residency" : {
        # solely from a 2021 Pew Research report
        "Urban" : 19,
        "Suburban" : 46,
        "Rural" : 35
    }
}

Bloc.instances = []
for group in blocs:
    for trait in blocs.get(group):
        Bloc(trait, blocs.get(group).get(trait))

'''
voting blocs


how to handle special relationships:
some blocs have greater-than-average correlation
for instance, 55% of latinos are catholic, compared to the national average of 22%
making a 2D array could work, but then there could be an overlap between three groups, like how 38% of south asian hindus have a higher degree
each new possibility of a relationship introduces a new dimension, so the total number is equal to the power set of all demographics, or 2^64
a 2D array would already have 64^2 cells to complete

could assume that relationships are equal to the national average by default, and add a separate list of larger outliers

additionally, this information has to be tracked to all 50+ states and territories, which have differing percentages

all states have a different percentage of these voting blocs



'''

state_demographics = { # this list was created with the aid of AI. There may be innaccuracies.
    "Alabama": {
        "Generation" : {
        },
        "Family" : {
        },
        "Religion" : {
            "Evangelical" : 49,
            "Mainline Protestant" : 13,
            "Black Protestant" : 16,
            "Catholic" : 7,
            "Mormon" : 1,
            "Jehovah's Witness" : 0,
            "Orthodox Christian" : 0,
            "Unaffiliated" : 9,
            "Agnostic" : 1,
            "Atheist" : 1,
            "Jewish" : 1,
            "Muslim" : 0.4,
            "Buddhist" : 0.4,
            "Hundu" : 0.2,
            "Other Non-Christian" : 1
        },
        "Education" : { # adapted from https://www.census.gov/quickfacts/fact/table/AL/PST045223
            "Primary Education" : 10.8,
            "Secondary Education" : 42.4,
            "Tertiary Education" : 19.6,
            "College Degree" : 19.1,
            "Higher Degree" : 8.1
        },
        "Race & Ethnicity": {
            "European (White)": 68.5,
            "African American (Black)": 26.8,
            "Hispanic / Latino": 4.6,
            "Asian": 1.5,
            "Other": 4.5
        },
        "Employment" : {
        },
        "Residency" : {
        }
    },
    "Alaska": {
        "ethnicity": {
            "White": 60.3,
            "Native American": 15.6,
            "Hispanic": 7.0,
            "Asian": 6.1,
            "Black": 3.8,
            "Pacific Islander": 1.3,
            "Other": 6.0
        }
    },
    "Arizona": {
        "ethnicity": {
            "White": 77.4,
            "Hispanic": 31.7,
            "Black": 4.9,
            "Asian": 3.7,
            "Native American": 5.3,
            "Other": 8.0
        }
    },
    "Arkansas": {
        "ethnicity": {
            "White": 77.0,
            "Black": 15.7,
            "Hispanic": 8.7,
            "Asian": 1.8,
            "Other": 2.6
        }
    },
    "California": {
        "ethnicity": {
            "White": 36.8,
            "Hispanic": 39.4,
            "Asian": 15.5,
            "Black": 6.5,
            "Other": 12.3
        }
    },
    "Colorado": {
        "ethnicity": {
            "White": 86.2,
            "Hispanic": 21.7,
            "Black": 4.5,
            "Asian": 3.5,
            "Other": 5.2
        }
    },
    "Connecticut": {
        "ethnicity": {
            "White": 66.9,
            "Hispanic": 16.5,
            "Black": 11.8,
            "Asian": 5.9,
            "Other": 7.5
        }
    },
    "Delaware": {
        "ethnicity": {
            "White": 68.9,
            "Black": 22.3,
            "Hispanic": 10.2,
            "Asian": 4.1,
            "Other": 4.5
        }
    },
    "District of Columbia": {
        "ethnicity": {
            "Black": 45.8,
            "White": 36.1,
            "Hispanic": 11.3,
            "Asian": 4.3,
            "Other": 2.5
        }
    },
    "Florida": {
        "ethnicity": {
            "White": 77.6,
            "Hispanic": 26.1,
            "Black": 16.9,
            "Asian": 3.0,
            "Other": 8.2
        }
    },
    "Georgia": {
        "ethnicity": {
            "Black": 32.6,
            "White": 51.8,
            "Hispanic": 9.9,
            "Asian": 4.3,
            "Other": 2.5
        }
    },
    "Hawaii": {
        "ethnicity": {
            "Asian": 37.6,
            "White": 26.7,
            "Two or more races": 24.9,
            "Native Hawaiian and Other Pacific Islander": 10.3,
            "Hispanic": 10.0,
            "Other": 9.0
        }
    },
    "Idaho": {
        "ethnicity": {
            "White": 82.4,
            "Hispanic": 13.9,
            "Native American": 1.7,
            "Asian": 1.5,
            "Other": 3.5
        }
    },
    "Illinois": {
        "ethnicity": {
            "White": 72.4,
            "Hispanic": 17.4,
            "Black": 14.6,
            "Asian": 5.9,
            "Other": 8.2
        }
    },
    "Indiana": {
        "ethnicity": {
            "White": 85.6,
            "Black": 9.8,
            "Hispanic": 7.4,
            "Asian": 2.8,
            "Other": 2.7
        }
    },
    "Iowa": {
        "ethnicity": {
            "White": 85.1,
            "Hispanic": 6.2,
            "Black": 4.2,
            "Asian": 2.7,
            "Other": 3.8
        }
    },
    "Kansas": {
        "ethnicity": {
            "White": 77.4,
            "Hispanic": 11.9,
            "Black": 6.1,
            "Asian": 3.2,
            "Other": 4.4
        }
    },
    "Kentucky": {
        "ethnicity": {
            "White": 85.3,
            "Black": 8.4,
            "Hispanic": 4.8,
            "Asian": 1.4,
            "Other": 3.0
        }
    },
    "Louisiana": {
        "ethnicity": {
            "White": 59.7,
            "Black": 32.8,
            "Hispanic": 8.7,
            "Asian": 1.8,
            "Other": 3.2
        }
    },
    "Maine": {
        "ethnicity": {
            "White": 93.6,
            "Hispanic": 1.6,
            "Black": 1.3,
            "Asian": 1.1,
            "Other": 2.4
        }
    },
    "Maryland": {
        "ethnicity": {
            "White": 58.5,
            "Black": 31.1,
            "Hispanic": 11.8,
            "Asian": 6.8,
            "Other": 5.2
        }
    },
    "Massachusetts": {
        "ethnicity": {
            "White": 74.1,
            "Hispanic": 12.5,
            "Black": 8.6,
            "Asian": 7.6,
            "Other": 10.0
        }
    },
    "Michigan": {
        "ethnicity": {
            "White": 75.8,
            "Black": 14.2,
            "Hispanic": 5.3,
            "Asian": 3.3,
            "Other": 5.0
        }
    },
    "Minnesota": {
        "ethnicity": {
            "White": 82.8,
            "Hispanic": 6.7,
            "Black": 6.1,
            "Asian": 5.6,
            "Other": 4.2
        }
    },
    "Mississippi": {
        "ethnicity": {
            "White": 57.6,
            "Black": 37.6,
            "Hispanic": 3.1,
            "Asian": 1.1,
            "Other": 2.1
        }
    },
    "Missouri": {
        "ethnicity": {
            "White": 79.2,
            "Black": 11.8,
            "Hispanic": 4.0,
            "Asian": 2.6,
            "Other": 3.4
        }
    },
    "Montana": {
        "ethnicity": {
            "White": 88.7,
            "Native American": 6.7,
            "Hispanic": 3.9,
            "Asian": 0.7,
            "Other": 2.4
        }
    },
    "Nebraska": {
        "ethnicity": {
            "White": 82.1,
            "Hispanic": 11.3,
            "Black": 5.0,
            "Asian": 2.6,
            "Other": 3.4
        }
    },
    "Nevada": {
        "ethnicity": {
            "White": 49.9,
            "Hispanic": 29.0,
            "Black": 9.1,
            "Asian": 8.1,
            "Other": 7.6
        }
    },
    "New Hampshire": {
        "ethnicity": {
            "White": 90.8,
            "Hispanic": 3.9,
            "Black": 1.7,
            "Asian": 3.3,
            "Other": 2.3
        }
    },
    "New Jersey": {
        "ethnicity": {
            "White": 55.8,
            "Hispanic": 20.6,
            "Black": 15.1,
            "Asian": 10.5,
            "Other": 12.8
        }
    },
    "New Mexico": {
        "ethnicity": {
            "Hispanic": 49.0,
            "White": 36.2,
            "Native American": 9.5,
            "Black": 2.6,
            "Asian": 1.8,
            "Other": 7.9
        }
    },
    "New York": {
        "ethnicity": {
            "White": 55.8,
            "Hispanic": 19.2,
            "Black": 17.6,
            "Asian": 9.0,
            "Other": 16.5
        }
    },
    "North Carolina": {
        "ethnicity": {
            "White": 67.2,
            "Black": 22.2,
            "Hispanic": 9.6,
            "Asian": 3.1,
            "Other": 4.8
        }
    },
    "North Dakota": {
        "ethnicity": {
            "White": 84.2,
            "Native American": 5.0,
            "Hispanic": 2.5,
            "Black": 2.0,
            "Asian": 1.4,
            "Other": 3.1
        }
    },
    "Ohio": {
        "ethnicity": {
            "White": 81.9,
            "Black": 13.0,
            "Hispanic": 4.3,
            "Asian": 2.3,
            "Other": 2.4
        }
    },
    "Oklahoma": {
        "ethnicity": {
            "White": 72.5,
            "Native American": 7.9,
            "Hispanic": 10.5,
            "Black": 7.0,
            "Asian": 2.1,
            "Other": 8.2
        }
    },
    "Oregon": {
        "ethnicity": {
            "White": 79.3,
            "Hispanic": 13.1,
            "Asian": 4.6,
            "Black": 2.2,
            "Other": 4.8
        }
    },
    "Pennsylvania": {
        "ethnicity": {
            "White": 78.4,
            "Black": 11.0,
            "Hispanic": 7.0,
            "Asian": 3.8,
            "Other": 4.8
        }
    },
    "Rhode Island": {
        "ethnicity": {
            "White": 73.0,
            "Hispanic": 15.0,
            "Black": 7.7,
            "Asian": 3.4,
            "Other": 5.4
        }
    },
    "South Carolina": {
        "ethnicity": {
            "White": 68.2,
            "Black": 27.1,
            "Hispanic": 6.7,
            "Asian": 1.8,
            "Other": 4.0
        }
    },
    "South Dakota": {
        "ethnicity": {
            "White": 84.9,
            "Native American": 9.4,
            "Hispanic": 3.7,
            "Black": 1.7,
            "Asian": 1.3,
            "Other": 2.7
        }
    },
    "Tennessee": {
        "ethnicity": {
            "White": 76.3,
            "Black": 17.1,
            "Hispanic": 5.6,
            "Asian": 1.9,
            "Other": 2.9
        }
    },
    "Texas": {
        "ethnicity": {
            "White": 41.2,
            "Hispanic": 39.6,
            "Black": 12.9,
            "Asian": 5.8,
            "Other": 9.0
        }
    },
    "Utah": {
        "ethnicity": {
            "White": 86.9,
            "Hispanic": 14.2,
            "Black": 1.5,
            "Asian": 2.8,
            "Other": 4.2
        }
    },
    "Vermont": {
        "ethnicity": {
            "White": 93.1,
            "Hispanic": 1.9,
            "Black": 1.2,
            "Asian": 1.0,
            "Other": 2.1
        }
    },
    "Virginia": {
        "ethnicity": {
            "White": 61.4,
            "Black": 19.9,
            "Hispanic": 9.8,
            "Asian": 7.1,
            "Other": 5.6
        }
    },
    "Washington": {
        "ethnicity": {
            "White": 68.6,
            "Hispanic": 13.3,
            "Asian": 9.4,
            "Black": 4.5,
            "Other": 8.5
        }
    },
    "West Virginia": {
        "ethnicity": {
            "White": 92.1,
            "Black": 3.5,
            "Hispanic": 1.5,
            "Asian": 0.8,
            "Other": 1.6
        }
    },
    "Wisconsin": {
        "ethnicity": {
            "White": 80.6,
            "Hispanic": 7.1,
            "Black": 6.3,
            "Asian": 3.5,
            "Other": 3.8
        }
    },
    "Wyoming": {
        "ethnicity": {
            "White": 84.9,
            "Hispanic": 9.2,
            "Black": 1.3,
            "Asian": 0.8,
            "Other": 2.4
        }
    },
    "American Samoa": {
        "ethnicity": {
            "Asian": 2.8,
            "Pacific Islander": 92.5,
            "White": 1.0,
            "Other": 3.7
        }
    },
    "Guam": {
        "ethnicity": {
            "Asian": 26.3,
            "Pacific Islander": 37.3,
            "White": 7.1,
            "Other": 29.3
        }
    },
    "Northern Mariana Islands": {
        "ethnicity": {
            "Asian": 50.2,
            "Pacific Islander": 35.3,
            "White": 1.3,
            "Other": 13.2
        }
    },
    "Puerto Rico": {
        "ethnicity": {
            "White": 75.8,
            "Black": 12.4,
            "Hispanic": 99.0,
            "Asian": 0.2,
            "Other": 0.4
        }
    },
    "Virgin Islands": {
        "ethnicity": {
            "Black": 76.2,
            "White": 15.6,
            "Hispanic": 5.5,
            "Asian": 0.9,
            "Other": 1.8
        }
    }
}

from state import State