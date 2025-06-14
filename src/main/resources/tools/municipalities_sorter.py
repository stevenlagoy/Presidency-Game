'''
This tool sorts the values within municipalities.json by their FIPS values
(with empty values placed last) and writes the sorted list to logs/output.txt
'''

def main() -> None:
    with open("src\\main\\resources\\map\\municipalities.json", 'r', encoding='utf-8') as file:
        contents = file.readlines()
    
    start_indices = {}

    i = 1
    while i < len(contents):
        if contents[i].find("{") != -1:
            FIPS = contents[i+3].split(":")[1].replace("\"","").replace(",","").strip()
            if (FIPS == ""):
                FIPS = "99-" + str(i).zfill(6)
            start_indices[FIPS] = i
        i += 1

    sorted_keys = list(start_indices.keys())
    sorted_keys.sort()
    start_indices = {i: start_indices[i] for i in sorted_keys}

    print(list(start_indices)[-10:])

    processed = []
    
    for key in start_indices:
        for j in range(13):
            processed.append(contents[start_indices[key] + j])
    
    with open("logs\\output.txt", 'w', encoding='utf-8') as file:
        for line in processed:
            file.write(line)

if __name__ == "__main__":
    main()