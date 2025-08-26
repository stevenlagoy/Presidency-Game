from typing import List, Dict, Tuple

def read_lines(filename: str) -> List[str]:
    with open(filename) as data:
        return data.readlines()
    
def process_colors(lines: List[str]) -> Dict[str, str]:
    result: Dict[str, str] = {}
    for line in lines:
        FIPS_code = line.split()[0].strip()
        color_code = line.split()[1].strip()
        result[FIPS_code] = color_code
    return result

def add_colors(lines: List[str], colors: Dict[str, str]) -> List[str]:
    result: List[str] = []
    for line in lines:
        if "FIPS" in line:
            FIPS_code = line.split(":",1)[1].replace('"','').replace(',','').strip()
            color_code = "#FF007F" # debug pink, to spot later
            try:
                color_code = colors[FIPS_code]
            except KeyError as e:
                print("KeyError: the following FIPS code was invalid: " + str(e))
                print(line.strip())
            result += line
            result += line.split("FIPS",1)[0] + "color\" : \"" + color_code + "\",\n"
        else:
            result += line
    return result

def output_results(lines: List[str]) -> None:
    with open("logs\\output.txt",'w') as out:
        for line in lines:
            out.write(line)

def check_uniqueness(colors: List[str]) -> Tuple[bool, List[str]]:
    colorset: List[str] = []
    duplicates: List[str] = []
    for color in colors:
        if color not in colorset:
            colorset.append(color)
        else:
            duplicates.append(color)
    return (len(duplicates) == 0, duplicates)

def main():
    print("Reading data...")
    colors_data = read_lines("src\\main\\resources\\map\\counties_colors.txt")
    counties_data = read_lines("src\\main\\resources\\tools\\counties_data.json")
    print("Data read successfully")
    print("Processing colors...")
    colors = process_colors(colors_data)
    print("Colors processed")
    print("Checking uniqueness")
    unique, duplicates = check_uniqueness(list(colors.values()))
    if not unique:
        print("Duplicate entries found: " + str(duplicates))
    else:
        print("Colors are unique")
    print("Generating new data...")
    new_counties = add_colors(counties_data, colors)
    print("Data complete")
    print("Writing results")
    output_results(new_counties)
    print("Done")

if __name__ == "__main__":
    main()