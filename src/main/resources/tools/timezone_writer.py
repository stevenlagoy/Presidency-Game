'''
This tool adds timezone information to municipalities.json, written to logs/output.txt
This tool will also determine summer (daylight savings) time based on time zone
Some timezones will be written as "RESOLVE", which must be manually resolved
'''

timezones = {
    "Alabama" : "CST",
    "Alaska" : "AKST",
    "American Samoa" : "SST",
    "Arizona" : "MST",
    "Arkansas" : "CST",
    "California" : "PST",
    "Colorado" : "MST",
    "Connecticut" : "EST",
    "District of Columbia" : "EST",
    "Delaware" : "EST",
    "Florida" : "EST",
    "Georgia" : "EST",
    "Guam" : "ChST",
    "Hawaii" : "HST",
    "Idaho" : "MST",
    "Illinois" : "CST",
    "Indiana" : "EST",
    "Iowa" : "CST",
    "Kansas" : "CST",
    "Kentucky" : "EST",
    "Louisiana" : "CST",
    "Maine" : "EST",
    "Maryland" : "EST",
    "Massachusetts" : "EST",
    "Michigan" : "EST",
    "Minnesota" : "CST",
    "Mississippi" : "CST",
    "Missouri" : "CST",
    "Montana" : "MST",
    "Nebraska" : "CST",
    "Nevada" : "PST",
    "New Hampshire" : "EST",
    "New Jersey" : "EST",
    "New Mexico" : "MST",
    "New York" : "EST",
    "North Carolina" : "EST",
    "North Dakota" : "CST",
    "Northern Mariana Islands" : "ChST",
    "Ohio" : "EST",
    "Oklahoma" : "CST",
    "Oregon" : "PST",
    "Pennsylvania" : "EST",
    "Puerto Rico" : "AST",
    "Rhode Island" : "EST",
    "South Carolina" : "EST",
    "South Dakota" : "CST",
    "Tennessee" : "CST",
    "Texas" : "CST",
    "Utah" : "MST",
    "Virginia" : "EST",
    "U.S. Virgin Islands" : "AST",
    "Vermont" : "EST",
    "Washington" : "PST",
    "Wisconsin" : "CST",
    "West Virginia" : "EST",
    "Wyoming" : "MST",
}

counties_timezones = {
    "Aleutians West Census Area, Alaska" : "HST",
    "Escambia County, Florida" : "CST",
    "Santa Rosa County, Florida" : "CST",
    "Okaloosa County, Florida" : "CST",
    "Walton County, Florida" : "CST",
    "Holmes County, Florida" : "CST",
    "Washington County, Florida" : "CST",
    "Bay County, Florida" : "CST",
    "Jackson County, Florida" : "CST",
    "Calhoun County, Florida" : "CST",
    "Gulf County, Florida" : "RESOLVE",
    "Boundary County, Idaho" : "PST",
    "Bonner County, Idaho" : "PST",
    "Kootenai County, Idaho" : "PST",
    "Shoshone County, Idaho" : "PST",
    "Benewah County, Idaho" : "PST",
    "Latah County, Idaho" : "PST",
    "Clearwater County, Idaho" : "PST",
    "Nez Perce County, Idaho" : "PST",
    "Lewis County, Idaho" : "PST",
    "Idaho County, Idaho" : "RESOLVE",
    "Lake County, Indiana" : "CST",
    "Porter County, Indiana" : "CST",
    "LaPorte County, Indiana" : "CST",
    "Starke County, Indiana" : "CST",
    "Pulaski County, Indiana" : "CST",
    "Jasper County, Indiana" : "CST",
    "Newton County, Indiana" : "CST",
    "Posey County, Indiana" : "CST",
    "Venderburgh County, Indiana" : "CST",
    "Warrick County, Indiana" : "CST",
    "Spencer County, Indiana" : "CST",
    "Perry County, Indiana" : "CST",
    "Gibson County, Indiana" : "CST",
    "Pike County, Indiana" : "CST",
    "Dubois County, Indiana" : "CST",
    "Knox County, Indiana" : "CST",
    "Daviess County, Indiana" : "CST",
    "Martin County, Indiana" : "CST",
    "Sherman County, Kansas" : "MST",
    "Wallace County, Kansas" : "MST",
    "Greeley County, Kansas" : "MST",
    "Hamilton County, Kansas" : "MST",
    "Fulton County, Kentucky" : "CST",
    "Hickman County, Kentucky" : "CST",
    "Carlisle County, Kentucky" : "CST",
    "Ballard County, Kentucky" : "CST",
    "McCracken County, Kentucky" : "CST",
    "Graces County, Kentucky" : "CST",
    "Marshall County, Kentucky" : "CST",
    "Calloway County, Kentucky" : "CST",
    "Livingston County, Kentucky" : "CST",
    "Crittenden County, Kentucky" : "CST",
    "Lyon County, Kentucky" : "CST",
    "Caldwell County, Kentucky" : "CST",
    "Trigg County, Kentucky" : "CST",
    "Union County, Kentucky" : "CST",
    "Webster County, Kentucky" : "CST",
    "Hopkins County, Kentucky" : "CST",
    "Christian County, Kentucky" : "CST",
    "Henderson County, Kentucky" : "CST",
    "McLean County, Kentucky" : "CST",
    "Muhlenberg County, Kentucky" : "CST",
    "Todd County, Kentucky" : "CST",
    "Hancock County, Kentucky" : "CST",
    "Ohio County, Kentucky" : "CST",
    "Butler County, Kentucky" : "CST",
    "Logan County, Kentucky" : "CST",
    "Breckinridge County, Kentucky" : "CST",
    "Grayson County, Kentucky" : "CST",
    "Hart County, Kentucky" : "CST",
    "Edmonson County, Kentucky" : "CST",
    "Warren County, Kentucky" : "CST",
    "Simpson County, Kentucky" : "CST",
    "Barren County, Kentucky" : "CST",
    "Allen County, Kentucky" : "CST",
    "Green County, Kentucky" : "CST",
    "Adair County, Kentucky" : "CST",
    "Russell County, Kentucky" : "CST",
    "Clinton County, Kentucky" : "CST",
    "Metcalfe County, Kentucky" : "CST",
    "Monroe County, Kentucky" : "CST",
    "Cumberland County, Kentucky" : "CST",
    "Gogebic County, Michigan" : "CST",
    "Iron County, Michigan" : "CST",
    "Dickinson County, Michigan" : "CST",
    "Menominee County, Michigan" : "CST",
    "Sioux County, Nebraska" : "MST",
    "Scotts Bluff County, Nebraska" : "MST",
    "Banner County, Nebraska" : "MST",
    "Kimball County, Nebraska" : "MST",
    "Dawes County, Nebraska" : "MST",
    "Box Butte County, Nebraska" : "MST",
    "Morrill County, Nebraska" : "MST",
    "Cheyenne County, Nebraska" : "MST",
    "Sheridan County, Nebraska" : "MST",
    "Garden County, Nebraska" : "MST",
    "Deuel County, Nebraska" : "MST",
    "Dundy County, Nebraska" : "MST",
    "Chase County, Nebraska" : "MST",
    "Perkins County, Nebraska" : "MST",
    "Keith County, Nebraska" : "MST",
    "Arthur County, Nebraska" : "MST",
    "Grant County, Nebraska" : "MST",
    "Hooker County, Nebraska" : "MST",
    "Cherry County, Nebraska" : "RESOLVE",
    "Elko County, Nevada" : "RESOLVE",
    "Bowman County, North Dakota" : "MST",
    "Adams County, North Dakota" : "MST",
    "Slope County, North Dakota" : "MST",
    "Hettinger County, North Dakota" : "MST",
    "Grant County, North Dakota" : "MST",
    "Stark County, North Dakota" : "MST",
    "Billings County, North Dakota" : "MST",
    "Golden Valley County, North Dakota" : "MST",
    "Mercer County, North Dakota" : "MST",
    "Sioux County, North Dakota" : "RESOLVE",
    "McKenzie County, North Dakota" : "RESOLVE",
    "Dunn County, North Dakota" : "RESOLVE",
    "Malheur County, Oregon" : "RESOLVE",
    "Harding County, South Dakota" : "MST",
    "Butte County, South Dakota" : "MST",
    "Lawrence County, South Dakota" : "MST",
    "Pennington County, South Dakota" : "MST",
    "Custer County, South Dakota" : "MST",
    "Fall River County, South Dakota" : "MST",
    "Perkins County, South Dakota" : "MST",
    "Meade County, South Dakota" : "MST",
    "Shannon County, South Dakota" : "MST",
    "Ziebach County, South Dakota" : "MST",
    "Haakon County, South Dakota" : "MST",
    "Jackson County, South Dakota" : "MST",
    "Bennett County, South Dakota" : "MST",
    "Corson County, South Dakota" : "MST",
    "Dewey County, South Dakota" : "MST",
    "Stanley County, South Dakota" : "RESOLVE",
    "Scott County, Tennessee" : "EST",
    "Roane County, Tennessee" : "EST",
    "Rhea County, Tennessee" : "EST",
    "Hamilton County, Tennessee" : "EST",
    "Bradley County, Tennessee" : "EST",
    "Polk County, Tennessee" : "EST",
    "Meigs County, Tennessee" : "EST",
    "McMinn County, Tennessee" : "EST",
    "Monroe County, Tennessee" : "EST",
    "Loudon County, Tennessee" : "EST",
    "Blount County, Tennessee" : "EST",
    "Sevier County, Tennessee" : "EST",
    "Campbell County, Tennessee" : "EST",
    "Claiborne County, Tennessee" : "EST",
    "Hancock County, Tennessee" : "EST",
    "Hawkins County, Tennessee" : "EST",
    "Sullivan County, Tennessee" : "EST",
    "Johnson County, Tennessee" : "EST",
    "Carter County, Tennessee" : "EST",
    "Unicoi County, Tennessee" : "EST",
    "Washington County, Tennessee" : "EST",
    "Greene County, Tennessee" : "EST",
    "Cocke County, Tennessee" : "EST",
    "Anderson County, Tennessee" : "EST",
    "Union County, Tennessee" : "EST",
    "Knox County, Tennessee" : "EST",
    "Grainger County, Tennessee" : "EST",
    "Hamblen County, Tennessee" : "EST",
    "Jefferson County, Tennessee" : "EST",
    "El Paso County, Texas" : "MST",
    "Hudspeth County, Texas" : "MST",
}

''' These states do not observe daylight savings '''
no_daylight_savings_states = [
    "Arizona", # Except Navajo Nation
    "Guam",
    "Hawaii",
    "Northern Mariana Islands",
    "Puerto Rico",
    "U.S. Virgin Islands",
    "American Samoa",
]

standard_to_daylight = {
    "EST" : "EDT",
    "CST" : "CDT",
    "MST" : "MDT",
    "PST" : "PDT",
    "AKST" : "AKDT",
    "HST" : "HDT", # Aleutian islands only
}


def main() -> None:
    global timezones

    with open("src\\main\\resources\\map\\municipalities.json", 'r', encoding='utf-8') as file:
        contents = file.readlines()

    processed = ["{\n"]

    counties_checked = []

    i = 1
    state = ""
    while i < len(contents):
        if contents[i].find("{") != -1:
            state = contents[i+2].split(":")[1].replace("\"","").replace(",","").strip()
            county = contents[i].split(",")[1].strip()
            processed.append(contents[i])
        elif contents[i].find("time_zone") != -1:
            try:
                timezone = timezones[state]
                if county + ", " + state in counties_timezones.keys():
                    if county + ", " + state not in counties_checked:
                        counties_checked.append(county + ", " + state)
                    timezone = counties_timezones[county + ", " + state]
                processed.append(contents[i].replace("\"\"", "\"" + timezone + "\""))
                if state not in no_daylight_savings_states:
                    processed.append("\t\t\"daylight_timezone\" : \"" + standard_to_daylight[timezone] + "\",\n")
                else:
                    processed.append("\t\t\"daylight_timezone\" : \"" + timezone + "\",\n")
            except KeyError as e1:
                try:
                    timezone = contents[i].split(":")[1].replace("\"","").replace(",","").strip()
                    if timezone != "":
                        processed.append("\t\t\"daylight_timezone\" : \"" + standard_to_daylight[timezone] + "\",\n")
                except KeyError as e2:
                    print(e2)
        else:
            processed.append(contents[i])
        i += 1

    for county in counties_timezones.keys():
        if county not in counties_checked:
            print(county + " was never checked")


    with open("logs\\output.txt", 'w', encoding='utf-8') as file:
        for line in processed:
            if line.find("Cherry County, Nebraska") != -1:
                ...
            file.write(line)

if __name__ == "__main__":
    main()