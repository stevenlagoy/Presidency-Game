from typing import List, Dict, Tuple

class County:
  def __init__(self, descriptors: List[str] = [], FIPS: str = "", color_code: str = "", full_name: str = "", common_name: str = "", state: str = "", county_seat: str = "", population: int = 0, land_area: float = 0.0, classification: int = -1):
    self.descriptors = descriptors
    self.FIPS = FIPS
    self.color_code = color_code
    self.full_name = full_name
    self.common_name = common_name
    self.state = state
    self.county_seat = county_seat
    self.population = population
    self.land_area = land_area
    self.classification = classification
    
  def __str__(self):
    result = ""
    result += '\"' + self.full_name + ', ' + self.state + '\" : {\n'
    result += '\t\"descriptors\" : ' + str(self.descriptors) + ',\n'
    result += '\t\"FIPS\" : \"' + self.FIPS + '\",\n'
    result += '\t\"full_name\" : \"' + self.full_name + '\",\n'
    result += '\t\"common_name\" : \"' + self.common_name + '\",\n'
    result += '\t\"state\" : \"' + self.state + '\",\n'
    result += '\t\"county_seat\" : \"' + self.county_seat + '\",\n'
    result += '\t\"population\" : ' + str(self.population) + ',\n'
    result += '\t\"land_area\" : ' + str(self.land_area) + ',\n'
    result += '\t\"classification\" : ' + str(self.classification) + ',\n'
    result += '\t\"color_code\" : \"' + self.color_code + '\"\n'
    result += '},'
    return result

def readlines(filename: str) -> List[str]:
  with open(filename,'r') as data:
    return data.readlines()

def split_by_not_quoted(line: str, delimiter: str) -> List[str]:
  result = []
  in_quote = False
  prev_pos = 0
  i = 0
  while i < len(line):
    char = line[i]
    if char == '"':
      in_quote = not in_quote
    elif char == delimiter[0]:
      result.append(line[prev_pos:i])
    i += 1
  return result

def parse_list(line: str) -> List[str]:
  cleaned = line.replace('[','').replace(']','').strip()
  entries = split_by_not_quoted(cleaned, ",")
  return entries
  
def json_value(line: str) -> str:
  return line.replace(',','').split(":",1)[-1].replace('"','').strip()

county_words = ["County", "Parish", "City and Borough of", "Planning Region", "Municipality", "Independent City"]

def resolve_common_names(counties: Dict[str, County]) -> Dict[str, County]:
    print("Resolving county names")
    for county in counties:
        counties[county].common_name = counties[county].common_name.strip()
        if not counties[county].common_name or True in [word in counties[county].common_name for word in county_words]:
            cleaned = counties[county].full_name
            for word in county_words:
                if word in cleaned:
                    cleaned = cleaned.replace(word, '')
                    cleaned = cleaned.strip()
                    print("Removed " + word + " from " + cleaned)
            counties[county].common_name = cleaned
    return counties

def create_county(data: List[str]) -> Tuple[County, int]:
  result: County = County()
  i = 0
  for line in data:
    i += 1
    if "descriptors" in line:
      result.descriptors = parse_list(json_value(line))
    elif "FIPS" in line:
      result.FIPS = json_value(line)
    elif "color\"" in line:
      result.color_code = json_value(line)
    elif "full_name" in line:
      result.full_name = json_value(line)
    elif "common_name" in line:
      result.common_name = json_value(line)
    elif "state" in line:
      result.state = json_value(line)
    elif "county_seat" in line:
      result.county_seat= json_value(line)
    elif "population\"" in line:
      result.population = int(json_value(line))
    elif "land_area" in line:
      result.land_area = float(json_value(line))
    elif "}" in line:
      return result, i
  return result, i

def output_results(counties: List[County]) -> None:
  with open("logs\\output.txt",'w') as out:
    out.write("{\n")
    for county in counties:
      info = str(county)
      for line in info.split("\n"):
        out.write("\t" + line + "\n")
    out.write("}")

def main():
  print("Reading data...")
  county_data = readlines("src\\main\\resources\\tools\\counties_data.json")
  classification_data = readlines("src\\main\\resources\\tools\\classifications_data.txt")
  print("Read data successfully")
  counties: Dict[str, County] = {}
  i = 0
  prev_i = i
  while True:
    prev_i = i
    try:
      county, lines_read = create_county(county_data[i:i+60])
      i += lines_read
      counties[county.FIPS] = county
      # print(str(county))
      if prev_i == i:
        break
    except Exception as e:
    #   print(e)
      break
  print("Processed " + str(len(counties)) + " counties")
  
  counties = resolve_common_names(counties)
  print("Resolved county names")
    
  # Resolve county urban classification
  for line in classification_data:
    FIPS_code = line.split('|',1)[0].strip()
    classification_val = int(line.split('|',1)[-1])
    try:
      counties[FIPS_code].classification = classification_val
    except KeyError as e:
      print(str(e))
  print("Resolved classifications")
  
  output_results([counties[county] for county in counties])
    
  print("Done")
  
  
if __name__ == "__main__":
  main()