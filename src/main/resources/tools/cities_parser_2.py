from typing import List, Tuple
import math

'''https://en.wikipedia.org/wiki/Lists_of_populated_places_in_the_United_States'''

abbreviation_to_name = {
    "AK": "Alaska",
    "AL": "Alabama",
    "AR": "Arkansas",
    "AS": "American Samoa",
    "AZ": "Arizona",
    "CA": "California",
    "CO": "Colorado",
    "CT": "Connecticut",
    "DE": "Delaware",
    "DC": "District of Columbia",
    "FL": "Florida",
    "GA": "Georgia",
    "GU": "Guam GU",
    "HI": "Hawaii",
    "IA": "Iowa",
    "ID": "Idaho",
    "IL": "Illinois",
    "IN": "Indiana",
    "KS": "Kansas",
    "KY": "Kentucky",
    "LA": "Louisiana",
    "MA": "Massachusetts",
    "MD": "Maryland",
    "ME": "Maine",
    "MI": "Michigan",
    "MN": "Minnesota",
    "MO": "Missouri",
    "MP": "Northern Mariana Islands",
    "MS": "Mississippi",
    "MT": "Montana",
    "NC": "North Carolina",
    "ND": "North Dakota",
    "NE": "Nebraska",
    "NH": "New Hampshire",
    "NJ": "New Jersey",
    "NM": "New Mexico",
    "NV": "Nevada",
    "NY": "New York",
    "OH": "Ohio",
    "OK": "Oklahoma",
    "OR": "Oregon",
    "PA": "Pennsylvania",
    "PR": "Puerto Rico",
    "RI": "Rhode Island",
    "SC": "South Carolina",
    "SD": "South Dakota",
    "TN": "Tennessee",
    "TX": "Texas",
    "UT": "Utah",
    "VA": "Virginia",
    "VI": "U.S. Virgin Islands",
    "VT": "Vermont",
    "WA": "Washington",
    "WI": "Wisconsin",
    "WV": "West Virginia",
    "WY": "Wyoming",
}
name_to_abbreviation = {v: k for k, v in abbreviation_to_name.items()}

class City:
    instances: List["City"] = []
    def __init__(self, name: str, state:str, type_class: str, counties: List[str], population2020: int, population2010: int, land_area: float):
        self.name = name.replace("&#39;","'").replace("&#8216;","‘")
        self.state = state
        self.type_class = type_class
        self.counties = counties
        self.population2020 = population2020
        self.population2010 = population2010
        self.land_area = land_area
        City.instances.append(self)

    def to_json(self) -> List[str]:
        return [
            f"\"{self.name}, {self.counties[0]}, {name_to_abbreviation[self.state]}\" : {{",
            f"\t\"name\" : \"{self.name}\",",
            f"\t\"state\" : \"{self.state}\",",
            f"\t\"FIPS\" : \"{self.fips()}\",",
            f"\t\"counties\" : {str(self.counties).replace("'","\"")},",
            f"\t\"type_class\" : \"{self.type_class}\",",
            f"\t\"population_2010\" : {str(self.population2010)},", 
            f"\t\"population_2020\" : {str(self.population2020)},",
            f"\t\"population_2027\" : {str(self.estimate_pop(2027))},",
            f"\t\"land_area_2020\" : {str(self.land_area)}",
            "}"
        ]
    
    def fips(self) -> str | None:
        try:
            return FIPS[self.name + ", " + name_to_abbreviation[self.state]]
        except KeyError as e:
            if self.state != "Connecticut" and self.state != "Maine" and self.state != "Massachusetts":
                pass
            print(f"Could not find FIPS code for {self.name + ", " + name_to_abbreviation[self.state]}")
            return None

    def get_pop_density(self) -> float:
        return self.population2020 / self.land_area
    
    def get_growth_amt_annum(self) -> int:
        return (self.population2020 - self.population2010) / 10
    
    def get_growth_pct_annum(self) -> float:
        return (self.population2020 - self.population2010) / max(1, self.population2010) / 10
    
    def estimate_pop(self, year: int) -> int:
        if year < 2020:
            return self.population2020
        state_annum_growth = state_growth_map[self.state]
        local_annum_growth = max(min(self.get_growth_pct_annum(), 0.25), -0.25) # clamp to [-25%, 25%]
        adjusted_growth = local_annum_growth + state_annum_growth
        num_years = year - 2020
        expected_pop = round(self.population2020 * (1 + adjusted_growth) ** num_years)
        return expected_pop

    
    def get_state_total(self) -> int:
        state_total = 0
        for city in City.instances:
            if city.state == self.state:
                state_total += city.population2020
        return state_total

    def pct_state_total(self) -> float:
        return self.population2020 / self.get_state_total()

HTML_TAGS = [
    # Style and layout tags
    "<style data-mw-deduplicate=\"TemplateStyles:r1156832818\">.mw-parser-output .geo-default,.mw-parser-output .geo-dms,.mw-parser-output .geo-dec{display:inline}.mw-parser-output .geo-nondefault,.mw-parser-output .geo-multi-punct,.mw-parser-output .geo-inline-hidden{display:none}.mw-parser-output .longitude,.mw-parser-output .latitude{white-space:nowrap}</style>",
    "<span class=\"plainlinks nourlexpansion\">",
    "<span class=\"geo-inline\">",
    "<div>", "</div>",
    "<div class=\"center\">",
    
    # Table structure tags
    "<tr id=\"A\">", "<tr>", "</tr>",
    "<th scope=\"row\">", "</th>",
    "<td>", "</td>",
    "<td style=\"text-align:center;\">",
    "<td style=\"text-align:right;\">",
    "<td align=\"center\">",
    "<td align=\"right\">",
    
    # Text formatting tags
    "style=\"color:red\">",
    "style=\"color:green\">",
    "style=\"white-space:nowrap\">",
    "style=\"background:#eea;\">",
    "<span style=\"display:none\"",
    "<span style=\"color:green\">",
    "<span style=\"color:red\">",
    "<span", "</span>",
    "<p>", "</p>",
    "<b>", "</b>",
    
    # Links and references
    "<a href=",
    "<a class=\"external text\" href=",
    "<a class=\"mw-selflink-fragment\"",
    "<a rel=\"nofollow\" class=\"external text\" href=",
    "href=\"#Statutory_town\">",
    "</a>",
    
    # Other
    "data-sort-value=",
    "\n",
]

def clean_html_line(line: str) -> str:
    """Remove HTML tags and clean up a single line of text"""
    for tag in HTML_TAGS:
        line = line.replace(tag, "")
    return line.strip()

FIPS: dict[str, str] = {}
def read_FIPS():
    with open("src\\main\\resources\\FIPS.txt", "r", encoding="utf-8") as file:
        contents = file.readlines()
    for line in contents[1:]:
        columns = line.split("|")
        city_name: str = f"{" ".join(columns[4].split()[0:-1])}, {columns[0]}"
        fips: str = columns[1] + "-" + columns[2] 
        FIPS[city_name] = fips

def read_cities_data() -> List[str]:
    """Read and clean city data from input file, removing HTML tags"""
    with open("src\\main\\resources\\tools\\cities_data.in", "r", encoding="utf-8") as data:
        contents = data.readlines()
    
    return [
        cleaned_line
        for line in contents
        if (cleaned_line := clean_html_line(line))
    ]

def process_cities(data: List[str]) -> int:
    i = 0
    while i < len(data):
        line = data[i]
        if line.find("title=\"") != -1 and line.find("geohack") == -1:
            processed_city, lines_read = process_city(data[i:i+15])
            if processed_city is None:
                print("City could not be parsed. Returning")
                return 1
            i += lines_read
            continue
        i += 1
    return 0

def extract_city_data(data: List[str], i: int) -> Tuple[str, str, str, str, int, int, float, int] | None:
    """Extract city data from HTML lines and return as tuple, or None if parsing fails"""
    try:
        # Extract name and state from title
        title_parts = data[i].split("title=")[1].split(">")[0].split(", ")
        name = title_parts[0].replace("\"","").strip()
        state = title_parts[-1].replace("\"","").strip()
        

        if name == 'Poland':
            if state == 'Maine':
                pass

        if state == "Texas":
            pass

        try:
            indices = indices_map[state]
        except:
            print(f"Invalid state name: {state}")
            return None, 0
        
        lines_read = indices[0]

        city_type = read_city_type(data[i + indices[2]]) if isinstance(indices[2], int) else indices[2]
        if city_type in ("First", "Second", "Third", "Fourth"):
            city_type += " Class"
        try:
            city_types = city_types_map[state]
        except KeyError as e:
            city_types = None

        county = read_counties(data[i + indices[3]])
        if state == "Virginia" and city_type == "Independent City":
            counties = county
            county = []
            for c in counties:
                county.append(c + " Independent City")
        shift = 0
        while True:
            pop2020 = read_population(data[i + indices[4] + shift])
            pop2010 = read_population(data[i + indices[5] + shift])
            if (pop2020 is not None and pop2010 is not None or shift > 2):
                break
            shift += 1
        land_area = read_area(data[i + indices[6] + shift]) if isinstance(indices[6], int) else indices[6]
        if land_area is None:
            print(f"Could not read land area for {name}, {state}")
            land_area = 0.0
            lines_read -= 2

        if city_types is not None:
            for ct in city_types:
                if city_types[ct] < pop2020:
                    city_type = ct

        if (city_type is None or county is None or pop2020 is None or pop2010 is None or land_area is None):
            print(f"Error reading entries for: {data[i]}")
            return None, 0

        return name, state, city_type, county, pop2020, pop2010, land_area, lines_read
    
    except (ValueError, IndexError) as e:
        print(f"Error reading entries for: {data[i]}")
        return None, 0

def read_city_type(line: str) -> str:
    city_type = line.split("<")[0].split(">")[-1]
    if not city_type:
        city_type = line.split(">")[-1]
    return city_type.title()

def read_counties(line: str) -> List[str]:
    try:
        # Split on <br /> to handle multiple counties
        county_segments = line.split("<br />")
        counties = []
        
        for segment in county_segments:
            if 'title=' in segment:
                # Extract county name from title attribute
                county = segment.split('title=')[1].split('>')[0].replace('"','').split(',')[0].strip()
            else:
                # Fall back to raw text if no title attribute
                county = segment.split(">")[-1].strip()
            
            if county:
                counties.append(county)
                
        return counties if counties else [line.strip()]
    except (IndexError, AttributeError):
        return [line.strip()]

def read_population(line: str) -> str | None:
    try:
        return int(line.split(">")[-1].replace("\n","").replace(",","").split()[-1])
    except IndexError as e1:
        try:
            return int(line.split(">")[1].split("<")[0].replace(",",""))
        except ValueError as e2:
            try:
                return int(line.split("<")[0].replace(",",""))
            except ValueError as e3:
                return None
    except (ValueError) as e4:
        return None

def read_area(line: str) -> str | None:
    try:
        return float(line.split("&")[0].split(">")[1].split("/")[0].replace(",",""))
    except IndexError as e1:
        try:
            return float(line.split("<td ")[-1].split()[0].split("&")[0].replace(",","").replace("\"",""))
        except ValueError as e2:
            try:
                return float(line.split(">")[1].split("&")[0].replace(",",""))
            except (ValueError, IndexError) as e3:
                return None
    except ValueError as e4:
        try:
            return float(line.split()[0])
        except ValueError as e5:
            return None

def process_city(data: List[str]) -> Tuple[City, int] | None:
    '''Creates one city from HTML lines'''
    for i in range(len(data)):
        if "title=" not in data[i]:
            continue

        result = extract_city_data(data, i)
        if result[0] is None:
            return None, 0
        
        name, state, city_type, county, pop2020, pop2010, land_area, lines_read = result
        return City(name, state, city_type, county, pop2020, pop2010, land_area), lines_read
    
    return None, 0

# "state_name" : (lines_read, city_state, city_type, counties, pop2020, pop2010, land_area)
indices_map = {
    "Alabama" :         (9, 0, 1, 2, 3, 4, 8),
    "Alaska" :          (9, 0, 1, 2, 3, 4, 7),
    "Arizona" :         (9, 0, 1, 2, 3, 4, 8),
    "Arkansas" :        (9, 0, 1, 2, 3, 4, 8),
    "California" :      (9, 0, 1, 2, 3, 4, 8),
    "Colorado" :        (10, 0, 1, 2, 3, 4, 6),
    "Connecticut" :     (11, 0, 1, 10, 4, 5, 3),
    "Delaware" :        (9, 0, 1, 2, 3, 4, 8),
    "District of Columbia" : (6, 0, 1, 2, 3, 4, 5),
    "Florida" :         (9, 0, 6, 1, 2, 2, 3), # 2010 pop not listed
    "Georgia" :         (9, 0, 1, 2, 3, 4, 8),
    "Hawaii" :          (3, 0, "Census-Designated Place", 2, 1, 1, 0.0), # There are no incorporated cities in Hawaii # pop 2010 not listed # land area not listed
    "Idaho" :           (5, 0, "City", 4, 1, 2, 0.0), # All municipalities are called City # Areas not listed
    "Illinois" :        (9, 0, 1, 2, 3, 4, 8),
    "Indiana" :         (8, 0, 1, 5, 2, 3, 6),
    "Iowa" :            (7, 0, "City", 1, 2, 3, 4),  # All municipalities are called City
    "Kansas" :          (), # Kansas is not simply tablulated
    "Kentucky" :        (9, 0, 4, 8, 1, 2, 6),
    "Louisiana" :       (10, 0, 1, 2, 3, 4, 6),
    "Maine" :           (7, 0, 1, 2, 3, 3, 4), # 2010 pop not listed
    "Maryland" :        (9, 0, 1, 2, 3, 4, 6),
    "Massachusetts" :   (8, 0, 1, 2, 4, 4, 6), # 2010 pop not listed
    "Michigan" :        (10, 0, 1, 2, 3, 4, 7),
    "Minnesota" :       (6, 0, "", 4, 2, 2, 0.0), # City types are not listed but can be determined: https://www.house.mn.gov/hrd/pubs/cityclass.pdf # 2010 pop is not listed, but 2024 estimate is # Land Areas not listed
    "Mississippi" :     (10, 0, 1, 2, 3, 4, 6),
    "Missouri" :        (8, 0, 3, 1, 4, 4, 0.0), # pop 2010 is not listed but 2021 estimate is # land area not listed
    "Montana" :         (9, 0, 1, 2, 3, 4, 6),
    "Nebraska" :        (7, 0, 1, 5, 3, 3, 0.0), # pop 2010 is not listed but 2023 estimate is # land area not listed
    "Nevada" :          (10, 0, 1, 2, 3, 4, 6),
    "New Hampshire" :   (8, 0, 1, 2, 4, 4, 5), # pop 2010 is not listed
    "New Jersey" :      (10, 0, 1, 2, 3, 4, 6),
    "New Mexico" :      (10, 0, 1, 3, 4, 5, 7),
    "New York" :        (6, 0, 5, 1, 2, 2, 3), # pop 2010 not listed
    "North Carolina" :  (), # North Carolina is not simply tablulated
    "North Dakota" :    (6, 0, "City", 4, 2, 2, 0.0), # All municipalities are Cities # pop 2010 not listed, but 2024 estimate is # land area not listed
    "Ohio" :            (6, 0, 1, 5, 2, 3, 0.0), # land area not listed
    "Oklahoma" :        (6, 0, "", 4, 2, 2, 0.0), # City types must be determined # pop 2010 not listed, but 2022 estimate is # land area not listed
    "Oregon" :          (), # Oregon is not simply tablulated
    "Pennsylvania" :    (8, 0, 3, 2, 4, 4, 6), # All entries are Cities, but their class is also listed # pop 2010 not listed
    "Puerto Rico" :     (11, 0, 4, 0, 6, 7, 8), # Puerto Rico's municipalities are county equivalents
    "Rhode Island" :    (12, 0, 1, 2, 4, 5, 7),
    "South Carolina" :  (10, 0, 1, 3, 4, 5, 7),
    "South Dakota" :    (6, 0, "City", 4, 2, 2, 0.0), # Municipalities in South Dakota can also be incorporated as towns, Wentworth Lake County is an Incorporated Village # pop 2010 not listed, but 2024 estimate is # land area not listed
    "Tennessee" :       (7, 0, "City", 1, 2, 2, 3), # Cities and Towns are not legally distinct in Tennessee # pop 2010 not listed
    "Texas" :           (8, 0, 1, 2, 4, 4, 0.0), # Counties are tracked in two columns # pop 2010 not listed, but 2024 estimate is # land area not listed
    "Utah" :            (9, 0, 2, 1, 3, 3, 4), # pop 2010 not listed
    "Vermont" :         (6, 0, 1, 2, 3, 3, 4), # pop 2010 not listed
    "Virginia" :        (9, 0, "Independent City", 0, 6, 6, 7), # Independent cities serve as their own county-equivalent # Only pop 2024 estimate is listed
    "Washington" :      (10, 0, 1, 2, 3, 4, 6),
    "West Virginia" :   (6, 0, 1, 5, 3, 3, 0.0), # pop 2010 not listed, but 2023 estimate is # land area not listed
    "Wisconsin" :       (6, 0, "City", 1, 3, 2, 0.0), # There are separate tables for towns and villages
    "Wyoming" :         (9, 0, 1, 2, 3, 4, 6),
}

# Inner values are minimum population for the key classification
# Ensure values are sorted from smallest to largest
city_types_map = {
    "Minnesota" : {
        "Fourth Class" : 0,
        "Third Class" : 10_001,
        "Second Class" : 20_001,
        "First Class" : 100_000,
    },
    "Oklahoma" : {
        "Town" : 0,
        "City" : 1000,
    },
}

'''Estimations for population growth per annum, based on 2020 to 2024 estimations (when unavailable, 2010 to 2020)'''
state_growth_map = {
    "Alabama" :         2.7 / 4 / 100,
    "Alaska" :          0.9 / 4 / 100,
    "American Samoa" : -10.5 / 4 / 100,
    "Arizona" :         6.0 / 4 / 100,
    "Arkansas" :        2.6 / 4 / 100,
    "California" :     -0.3 / 4 / 100,
    "Colorado" :        3.2 / 4 / 100,
    "Connecticut" :     1.9 / 4 / 100,
    "Delaware" :        6.3 / 4 / 100,
    "District of Columbia" : 1.8 / 4 / 100,
    "Florida" :         8.5 / 4 / 100,
    "Georgia" :         4.4 / 4 / 100,
    "Guam" :            5.7 / 10 / 100,
    "Hawaii" :         -0.6 / 4 / 100,
    "Idaho" :           8.8 / 4 / 100,
    "Illinois" :       -0.8 / 4 / 100,
    "Indiana" :         2.0 / 4 / 100,
    "Iowa" :            1.6 / 4 / 100,
    "Kansas" :          1.1 / 4 / 100,
    "Kentucky" :        1.8 / 4 / 100,
    "Louisiana" :      -1.3 / 4 / 100,
    "Maine" :           3.1 / 4 / 100,
    "Maryland" :        1.4 / 4 / 100,
    "Massachusetts" :   1.5 / 4 / 100,
    "Michigan" :        0.6 / 4 / 100,
    "Minnesota" :       1.5 / 4 / 100,
    "Mississippi" :    -0.6 / 4 / 100,
    "Missouri" :        2.8 / 4 / 100,
    "Montana" :         4.9 / 4 / 100,
    "Nebraska" :        2.2 / 4 / 100,
    "Nevada" :          5.2 / 4 / 100,
    "New Hampshire" :   2.3 / 4 / 100,
    "New Jersey" :      2.3 / 4 / 100,
    "New Mexico" :      2.8 / 4 / 100,
    "New York" :       -1.7 / 4 / 100,
    "North Carolina" :  5.8 / 4 / 100,
    "North Dakota" :    2.2 / 4 / 100,
    "Northern Mariana Islands" : -12.2 / 4 / 100,
    "Ohio" :            0.7 / 4 / 100,
    "Oklahoma" :        3.4 / 4 / 100,
    "Oregon" :          0.8 / 4 / 100,
    "Pennsylvania" :    0.6 / 4 / 100,
    "Puerto Rico" :   -11.8 / 10 / 100,
    "Rhode Island" :    1.4 / 4 / 100,
    "South Carolina" :  7.0 / 4 / 100,
    "South Dakota" :    4.3 / 4 / 100,
    "Tennessee" :       4.6 / 4 / 100,
    "Texas" :           7.4 / 4 / 100,
    "Utah" :            7.1 / 4 / 100,
    "Vermont" :         0.8 / 4 / 100,
    "Virgin Islands" : -18.1 / 10 / 100,
    "Virginia" :        2.1 / 4 / 100,
    "Washington" :      3.3 / 4 / 100,
    "West Virginia" :  -1.3 / 4 / 100,
    "Wisconsin" :       1.1 / 4 / 100,
    "Wyoming" :         1.9 / 4 / 100,
}

'''most recent estimations for state populations (2024)'''
states_pops = {
    "Alabama" :         5257699,
    "Alaska" :          740133,
    "American Samoa" :  47521,
    "Arizona" :         7582384,
    "Arkansas" :        3088354,
    "California" :      39431263,
    "Colorado" :        5957493,
    "Connecticut" :     3675069,
    "Delaware" :        1051917,
    "District of Columbia" : 702250,
    "Florida" :         23372215,
    "Georgia" :         11180878,
    "Guam" :            172952,
    "Hawaii" :          1446146,
    "Idaho" :           2001619,
    "Illinois" :        12710158,
    "Indiana" :         6924275,
    "Iowa" :            3241488,
    "Kansas" :          2970606,
    "Kentucky" :        4588372,
    "Louisiana" :       4597740,
    "Maine" :           1405012,
    "Maryland" :        6263220,
    "Massachusetts" :   7136171,
    "Michigan" :        10140459,
    "Minnesota" :       5793151,
    "Mississippi" :     2943045,
    "Missouri" :        6245466,
    "Montana" :         1137233,
    "Nebraska" :        2005465,
    "Nevada" :          3267467,
    "New Hampshire" :   1409032,
    "New Jersey" :      9500851,
    "New Mexico" :      2130256,
    "New York" :        19867248,
    "North Carolina" :  11046024,
    "North Dakota" :    796568,
    "Northern Mariana Islands" : 45143,
    "Ohio" :            11883304,
    "Oklahoma" :        4095393,
    "Oregon" :          4272371,
    "Pennsylvania" :    13078751,
    "Puerto Rico" :     3203295,
    "Rhode Island" :    1112308,
    "South Carolina" :  5478831,
    "South Dakota" :    924669,
    "Tennessee" :       7227750,
    "Texas" :           31290831,
    "Utah" :            3503613,
    "Vermont" :         648493,
    "Virgin Islands" :  104917,
    "Virginia" :        8811195,
    "Washington" :      7958180,
    "West Virginia" :   1769979,
    "Wisconsin" :       5960975,
    "Wyoming" :         587618,
}

def generate_json(cities: List[City]) -> List[str]:
    return [
        line 
        for city in cities 
        if city is not None
        for line in city.to_json()
    ]

def write_output(lines: List[str]) -> None:
    with open("src\\main\\resources\\tools\\cities_data.out", "w", encoding="utf-8") as file:
        file.write("{\n")
        number_lines = len(lines)
        count = 0
        for line in lines:
            count += 1
            file.write("\t" + line.replace("&#39;","'"))
            if line.startswith("}") and count < number_lines:
                file.write(",")
            file.write("\n")

        file.write("}")

def main() -> int:
    read_FIPS()

    data: List[str] = read_cities_data()
    code = process_cities(data)
    if code != 0:
        return code

    print("Cities processed successfully.")

    lines: List[str] = generate_json(City.instances)
    write_output(lines)

    print("File generated successfully.")

    return 0

if __name__ == "__main__":
    code: int = main()
    print("Exiting with code: " + str(code))