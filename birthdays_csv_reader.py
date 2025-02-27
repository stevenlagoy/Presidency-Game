filename = "birthdays_popularity.csv"
outputfilename = "birthdate_popularities.JSON"

with open(filename) as file:
    contents = file.read()

contents = contents.split()[1:]

table = {}

total = 0
for line in contents:
    #print(line)
    line = line.split(",")
    year = line[0]
    month = line[1]
    day_of_month = line[2]
    day_of_week = line[3]
    try:
        births = int(line[4])
        total += births
    except:
        print("Cannot cast to int: " + line[4])
        break
    
    key = year + "-" + month + "-" + day_of_month
    table[key] = births

total = 0
num_days = 0
new_table = {}

for entry in table:
    total += table[entry]
    num_days += 1
    date = entry[5:]
    if(date == "2-29"):
        print(table[entry])
    if date not in table:
        new_table[date] = table[entry]
    else:
        new_table[date] += table[entry]

#print(new_table)
print("total births = " + str(total))
print("num days = " + str(num_days))
print("births per day = " + str(total / num_days))

values_table = {}
for entry in new_table:
    values_table[entry] = new_table[entry] / (total / num_days)

sum = 0
for entry in values_table:
    sum += values_table[entry]
print(sum)
#for entry in values_table:
    #values_table[entry] = values_table[entry] / (sum / 100)
sum = 0
for entry in values_table:
    sum += values_table[entry]

with open(outputfilename, "w") as file:
    file.write("{\n")
    for entry in values_table:
        file.write("\t\"" + entry + "\" : " + str(values_table[entry]) + ",\n")
    file.write("}")