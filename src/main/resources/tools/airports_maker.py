from typing import List


def read_data(filename: str) -> List[str]:
    with open(filename, 'r', encoding='utf-8') as data:
        return data.readlines()

def write_lines(filename: str, lines: List[str]) -> None:
    with open(filename, 'w', encoding='utf-8') as out:
        out.write("{\n")
        for line in lines:
            out.write("\t" + line)
        out.write("}")

def process_airport1(lines: List[str]) -> List[str]:
    i = 0
    while i < len(lines):
        if lines[i].find("title=") != -1:
            full_name = lines[i].split("title=")[1].split(">")[1].split("<")[0].strip()
            common_name = full_name.replace("Airport","").replace("International","").strip()
            iata = lines[i+2].split(">")[1].strip()
            city = lines[i+4].split("title=")[1].split(">")[1].split("<")[0].strip()
            state = lines[i+8].split(">")[1].strip()
            capacity = lines[i+10].split(">")[1].replace(",","").strip()
            if int(capacity) > 10_000_000:
                hub_size = "large"
            elif int(capacity) > 1_000_000:
                hub_size = "medium"
            else:
                hub_size = "small"
            
            processed = [
                f"\"{iata}\" : {{\n",
                f"\t\"full_name\" : \"{full_name}\",\n",
                f"\t\"common_name\" : \"{common_name}\",\n",
                f"\t\"IATA\" : \"{iata}\",\n",
                f"\t\"location\" : \"{city}, {state}\",\n",
                f"\t\"hub_size\" : \"{hub_size}\",\n",
                f"\t\"capacity\" : {capacity}\n",
                f"}},\n"
            ]
            return processed
        i += 1

def process_airport2(lines: List[str]) -> List[str]:
    i = 0
    while i < len(lines):
        try:
            if lines[i].find("title=") != -1:
                location = lines[i].split("title=\"")[1].split("\">")[0].strip()
                iata = lines[i+4].split(">")[-1].strip()
                full_name = lines[i+8].split("title=")[1].split(">")[1].split("<")[0].strip()
                common_name = full_name.replace("Airport","").replace("International","").strip()
                capacity = lines[i+12].split(">")[1].replace(",","").strip()
                if int(capacity) > 10_000_000:
                    hub_size = "large"
                elif int(capacity) > 1_000_000:
                    hub_size = "medium"
                else:
                    hub_size = "small"

                if "<" in iata:
                    ...

                processed = [
                    f"\"{iata}\" : {{\n",
                    f"\t\"full_name\" : \"{full_name}\",\n",
                    f"\t\"common_name\" : \"{common_name}\",\n",
                    f"\t\"IATA\" : \"{iata}\",\n",
                    f"\t\"location\" : \"{location}\",\n",
                    f"\t\"hub_size\" : \"{hub_size}\",\n",
                    f"\t\"capacity\" : {capacity}\n",
                    f"}},\n"
                ]
                return processed
        except IndexError as e:
            i += 1
            continue
        i += 1
    return None

def sort_capacities(lines: List[str]) -> List[str]:
    block_size = 8
    blocks = []
    i = 0
    while i < len(lines):
        block = lines[i:i+block_size]
        # Find the capacity line in the block
        for line in block:
            if "capacity" in line:
                capacity = int(line.split(":")[1].replace(",", "").replace("\n", "").strip())
                break
        else:
            capacity = 0  # fallback if not found
        blocks.append((capacity, block))
        i += block_size

    # Sort blocks by capacity descending
    blocks.sort(key=lambda x: x[0], reverse=True)

    # Eliminate duplicates based on IATA code
    seen_iata = set()
    sorted_lines = []
    for _, block in blocks:
        # IATA code is in the first line:  "\"XXX\" : {"
        if len(block) == 0:
            continue
        iata_line = block[0].strip()
        if iata_line.startswith('"') and '" : {' in iata_line:
            iata = iata_line.split('"')[1]
            if iata in seen_iata:
                continue
            seen_iata.add(iata)
        sorted_lines.extend(block)
    return sorted_lines

def main():
    contents = read_data("src\\main\\resources\\tools\\airports_data1.in")
    processed = []
    i = 0
    while i < len(contents):
        for line in process_airport1(contents[i:i+33]):
            processed.append(line)
        i += 33

    contents = read_data("src\\main\\resources\\tools\\airports_data2.in")
    i = 0
    while i < len(contents):
        lines = process_airport2(contents[i:i+15])
        if lines is None:
            i += 1
            continue
        for line in lines:
            processed.append(line)
        i += 15

    write_lines("logs\\output.txt", sort_capacities(processed))

if __name__ == "__main__":
    main()