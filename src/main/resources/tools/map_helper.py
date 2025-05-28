from typing import Dict, List

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

states_colors: Dict[str, str] = {}
counties_colors: Dict[str, str] = {}
states_counties: Dict[str, List[str]] = {}
fips: Dict[str, str] = {}
def read_data():
    global fips
    fips = {}
    with open("src\\main\\resources\\counties.json") as data:
        contents = data.readlines()
    name, code, state = None, None, None
    for line in contents:
        if line.find("name") != -1:
            name = line.replace("\"name\" : ","").replace(",","").replace("\"","").strip()
        elif line.find("FIPS") != -1:
            code = line.split()[-1].replace(",","").replace("\"","")
        elif line.find("state") != -1:
            state = line.replace("\"state\" : ","").replace(",","").replace("\"","").strip()
        if name and code and state:
            fips[name + ", " + state] = code
            try:
                states_counties[state].append(name)
            except KeyError as e:
                states_counties[state] = [name]
            name, code, state = None, None, None
    with open("src\\main\\resources\\states_colors.txt") as data:
        contents = data.readlines()
    for line in contents:
        if line:
            states_colors[line.split()[0]] = line.split()[-1]

hex_digits = {"0" : 0, "1" : 1, "2" : 2, "3" : 3, "4" : 4, "5" : 5, "6" : 6, "7" : 7,
    "8" : 8, "9" : 9, "A" : 10, "B" : 11, "C" : 12, "D" : 13, "E" : 14, "F" : 15,
}

def decimal_to_hex(decimal):
  if decimal == 0:
    return "0"
  hex_str = ""
  while decimal > 0:
    remainder = decimal % 16
    for k, v in hex_digits.items():
      if v == remainder:
        hex_str = k + hex_str
        break
    decimal //= 16
  return hex_str

def hex_to_decimal(hex_str):
  hex_str = hex_str.upper()
  decimal = 0
  power = 0
  for digit in reversed(hex_str):
    decimal += hex_digits[digit] * (16 ** power)
    power += 1
  return decimal

def hex_to_rgb(hex_code):
    hex_code = hex_code.lstrip("#")
    return tuple(int(hex_code[i:i+2], 16) for i in (0, 2, 4))

def rgb_to_hex(rgb):
    return '#' + decimal_to_hex(rgb[0]) + decimal_to_hex(rgb[1]) + decimal_to_hex(rgb[2])

def hsl_to_rgb(h, s, l):
  c = (1 - abs(2 * l - 1)) * s
  x = c * (1 - abs((h / 60) % 2 - 1))
  m = l - c / 2

  if 0 <= h < 60:
    r1, g1, b1 = c, x, 0
  elif 60 <= h < 120:
    r1, g1, b1 = x, c, 0
  elif 120 <= h < 180:
    r1, g1, b1 = 0, c, x
  elif 180 <= h < 240:
    r1, g1, b1 = 0, x, c
  elif 240 <= h < 300:
    r1, g1, b1 = x, 0, c
  else:
    r1, g1, b1 = c, 0, x

  r = int((r1 + m) * 255)
  g = int((g1 + m) * 255)
  b = int((b1 + m) * 255)
  return r, g, b

def generate_even_saturation_colors(n, saturation=0.8, lightness=0.5):
  colors = []
  for i in range(n):
    hue = (360 / n) * i
    r, g, b = hsl_to_rgb(hue, saturation, lightness)
    hex_r = decimal_to_hex(r).zfill(2)
    hex_g = decimal_to_hex(g).zfill(2)
    hex_b = decimal_to_hex(b).zfill(2)
    colors.append("#" + hex_r + hex_g + hex_b)
  return colors

def shift_color_toward(base_color, shift_color, amount=0.5):
    base_rgb = hex_to_rgb(base_color)
    shift_rgb = hex_to_rgb(shift_color)

    result_rgb = tuple(
        max(0, min(255, int(base + (target - base) * amount)))
        for base, target in zip(base_rgb, shift_rgb)
    )
    
    return rgb_to_hex(result_rgb)

def lighten(base_color):
   return shift_color_toward(base_color, "FFFFFF")

def modify_by_one(color_code):
    r, g, b = hex_to_rgb(color_code)
    
    # Try modifying each component by 1 until we get a valid color
    if r > 0:
        r -= 1
    elif g > 0:
        g -= 1
    elif b > 0:
        b -= 1
    else:
        # If we can't decrease, try increasing
        if r < 255:
            r += 1
        elif g < 255:
            g += 1
        elif b < 255:
            b += 1
            
    return rgb_to_hex((r, g, b))

colors: List[str] = []
def calculate_counties_colors():
    global counties_colors
    for state in states_counties.keys():
        shift_color = states_colors[name_to_abbreviation[state]]
        num_counties = len(states_counties[state])
        colors = generate_even_saturation_colors(num_counties, 1.0, 0.6)
        i = 0
        for county in states_counties[state]:
            calculated = shift_color_toward(colors[i], shift_color)
            while calculated in colors:
                print("Encountered color code clash with " + county + ", " + state)
                calculated = modify_by_one(calculated)
            colors.append(calculated)
            counties_colors[fips[county + ", " + state]] = calculated
            i += 1

def write_output():
    with open("src\\main\\resources\\counties_colors.txt", "w") as out:
        for county in counties_colors.keys():
            for key, value in fips.items():
                if value == county:
                    name = " ".join(key.split()[:-1])
                    state = key.split(",")[-1].strip()
            try:
                out.write(county + "\t\t" + counties_colors[county] + "\t\t" + name + " " + name_to_abbreviation[state] + "\n")
            except KeyError as e:
               print("State key error with " + state)

def main():
    read_data()
    calculate_counties_colors()
    write_output()

if __name__ == "__main__":
    main()