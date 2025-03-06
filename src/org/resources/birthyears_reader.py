current_year = 2023

with open("birth_numbers.txt") as file:
    contents = file.read()
contents = contents.split("\n")
fixed_list = []
for entry in contents:
    if entry != "":
        fixed_list.append(entry.replace("\t",",").replace("%",""))
print(fixed_list)

male_percentages = {}
female_percentages = {}

for i in range(1900, 2028):
    if i not in male_percentages:
        male_percentages[i] = 0.0
        female_percentages[i] = 0.0


for i in range(0,len(fixed_list)):
    male_percentages[current_year - int(fixed_list[i].split(",")[0])] = fixed_list[i].split(",")[1]
    female_percentages[current_year - int(fixed_list[i].split(",")[0])] = fixed_list[i].split(",")[2]

with open("birthyear_percentages.JSON","w") as file:
    file.write("{\n")
    file.write("\t\"male\": {\n")
    for key in male_percentages:
        file.write("\t\t\""+str(key)+"\": " + str(male_percentages[key]) + ",\n")
    file.write("\t},\n")
    file.write("\t\"female\": {\n")
    for key in female_percentages:
        file.write("\t\t\""+str(key)+"\": " + str(female_percentages[key]) + ",\n")
    file.write("\t}\n")
    file.write("}")