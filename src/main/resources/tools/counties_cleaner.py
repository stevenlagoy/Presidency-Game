from typing import Dict, List

def main():

    html_lines = read_html_lines()
    json_lines = read_county_json()

    county_seats = process_html_lines(html_lines)

    result = generate_output(json_lines, county_seats)

    write_to_output(result)

    print("Finished")


def read_html_lines() -> List[str]:
    with open("src\\main\\resources\\counties_html.in", "r", encoding="utf-8") as data:
        return data.readlines()

def process_html_lines(lines: List[str]) -> Dict[str, str]:
    processed = {}
    i = 0
    while i < len(lines):
        line = lines[i]
        if line.find("scope=\"row\"") != -1:
            try:
                county_name = line.split("title=")[1].split(">")[0].replace("\"","").strip()
                county_seat = lines[i+4].split("title=")[1].split(">")[0].replace("\"","").strip()
                processed[county_name] = county_seat
            except IndexError as e:
                print("Could not properly parse line: " + line)
                exit(2)
        i += 1

    return processed

def read_county_json() -> List[str]:
    with open("src\\main\\resources\\counties.json", "r") as counties_file:
        return counties_file.readlines()

def generate_output(base_lines: List[str], county_seats: Dict[str, str]) -> List[str]:
    result = []
    i = 0
    while i < len(base_lines):
        line = base_lines[i]
        if line.find("name") != -1:
            county_name = line.split(":")[1].replace("\"","").replace(",","").strip()
        elif line.find("state") != -1:
            state_name = line.split(":")[1].replace("\"","").replace(",","").strip()
        if line.find("county_seat") != -1:
            try:
                county_seat = county_seats[county_name + ", " + state_name]
                result.append("\t\t\"county_seat\" : \"" + county_seat.split(",")[0] + "\",\n")
            except KeyError as e:
                print("Could not find entry for: " + county_name + ", " + state_name)
                result.append("\t\t\"county_seat\" : \"COUNTY SEAT\",\n")
        else:
            result.append(line)
        i += 1
    return result

def write_to_output(lines: List[str]) -> None:
    with open("src\\main\\resources\\counties_result.out", "w", encoding="utf-8") as output:
        for line in lines:
            output.write(line)

if __name__ == "__main__":
    main()