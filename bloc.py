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
    "Generation" : [
        # solely from "Population Distribution inthe United States in 2022, by generation" published to Statista by Veera Korhonen
        # generation alpha has been given the remainder of 100 minus the sum of all other cohorts
        ("Greatest Generation", 0.20),
        ("Silent Generation", 5.49),
        ("Baby Boomers", 20.58),
        ("Generation X", 19.61),
        ("Millennial", 21.67),
        ("Generation Z", 20.88),
        ("Generation Alpha", 11.57)
    ],
    "Family" : [
        # primarily from "Marital status of the United States population in 2022, by sex" published to Statista by Veera Korhonen, and secondarily from "Americans' Preference for Larger Families Highest Since 1971" published to Gallup by Megan Brenan
        # percentages have been distributed according to the bottommost applicable classification
        # the control for sex has been reverted
        ("Single", 16.02),
        ("Married", 23.84),
        ("Widdowed", 2.66),
        ("Divorced", 4.48),
        ("At least one child", 32),
        ("Three or more children", 21)
    ],
    "Religion" : [
        # primarily from the PRRI American Values Atlas and secondarily from a 2014 Pew Research Center report
        # numbers for "unaffiliated" are distributed according to relative proportion, and "nothing in particular" has been called "unaffiliated" below
        # numbers for "Hispanic Protestant" and "other non-white Protestant" have been distributed between "Evangelical" (76%) and "Mainline Protestant" (24%) as reported by the Pew Research Center in 2013
        ("Evangelical", 20.1),
        ("Mainline Protestant", 18.2),
        ("Black Protestant", 7.3),
        ("Catholic", 21.8),
        ("Mormon", 1.3),
        ("Jehovah's Witness", 0.5),
        ("Orthodox Christian", 0.5),
        ("Unaffiliated", 16.1),
        ("Agnostic", 4.1),
        ("Atheist", 3.1),
        ("Jewish", 1.4),
        ("Muslim", 0.8),
        ("Buddhist", 0.8),
        ("Hundu", 0.5),
        ("Other Non-Christian", 3.5),
    ],
    "Education" : [
        # primarily from a 2022 report by the Census Bureau
        # persons under 25 years of age (12% pop) COULD be distributed into the four earliest education brackets @ 72%, 8%, 16%, and 4% respectively
        ("Primary Education", 8.8),
        ("Secondary Education", 27.9),
        ("Tertiary Education", 14.9),
        ("College Degree", 34.0),
        ("Higher Degree", 14.4)
    ],
    "Race & Ethnicity" : [
        # primarily from the Census Bureau's 2020 Census and American Community Survey Report 2022
        # these numbers have been adjusted to distribute "two or more races" according to their relative proportions
        ("European (White)", 59.63),
        ("African American (Black)", 12.23),
        ("Native American Indian", 0.71),
        ("East Asian", 2.54),
        ("South Asian", 2.53),
        ("Southeast Asian", 3.16),
        ("Pacific Islander", 0.24),
        ("Hispanic / Latino", 18.96)
    ],
    "Employment" : [
        # primarily from "Number of employees in the United States in December 2023, by industry" published by Statista Research Department
        # student numbers from the 2022 Current Population Survey
        # employed attendees of any public or private school or college are distributed only as "Student", even if otherwise employed
        ("Unemployed", 3.7),
        ("Student", 29.1),
        ("Professional & Business", 8.8825),
        ("Healthcare", 8.4738),
        ("Hospitality", 6.4964),
        ("Retail Trade", 6.0169),
        ("Local Government", 5.6951),
        ("Manufacturing", 5.0270),
        ("Construction", 3.1185),
        ("Finance & Insurance", 2.5931),
        ("Transportation & Warehousing", 2.5712),
        ("Wholesale Trade", 2.3610),
        ("Other Services", 2.2820),
        ("State Government", 2.0617),
        ("Private Education", 1.5435),
        ("Information", 1.1811),
        ("Federal Government", 1.1478),
        ("Real Estate", 0.9480),
        ("Resource Extraction", 0.2481),
        ("Utility", 0.2175),
        ("Other Sector", 6.3348)
    ],
    "Residency" : [
        # solely from a 2021 Pew Research report
        ("Urban", 19),
        ("Suburban", 46),
        ("Rural", 35)
    ]
}

'''
voting blocs


how to handle special relationships:
some blocs have greater-than-average correlation
for instance, 55% of latinos are catholic, compared to the national average of 22%
making a 2D array could work, but then there could be an overlap between three groups, like how 38% of south asian hindus have a higher degree
each new possibility of a relationship introduces a new dimension, so the total number is equal to the power set of all demographics, or 2^64
a 2D array would already have 64^2 cells to complete

could assume that relationships are equal to the national average by default, and add a separate list of larger outliers
'''