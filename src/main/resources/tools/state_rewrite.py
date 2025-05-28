from typing import List, Dict

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
    "GU": "Guam",
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

def read_file(file_name: str) -> List[str]:
    with open(file_name, "r", encoding="utf-8") as file:
        contents = file.readlines()
    return contents

def main() -> None:
    colors_lines = read_file("src\\main\\resources\\states_colors.txt")
    states_lines = read_file("src\\main\\resources\\states.json")

    states_colors = {}
    for line in colors_lines:
        if not line: continue
        states_colors[line.split()[0]] = line.split()[-1]
    
    result = []
    name = ""
    for line in states_lines:
        if line.find("name") != -1:
            name = line.split(":")[-1].replace("\"","").replace(",","").strip()
        if line.find("population") != -1:
            try:
                result.append(f"\t\t\"map_color\" : \"{states_colors[name_to_abbreviation[name]]}\",\n")
                result.append(f"\t\t\"flag_loc\" : \"{name.lower().replace(" ","_") + "_flag.png"}\",\n")
            except KeyError as e:
                print(str(e))
        result.append(line)

    with open("src\\main\\resources\\states.json", "w", encoding="utf-8") as out:
        for line in result:
            out.write(line)

    return 0

if __name__ == "__main__":
    main()
    print("Done")