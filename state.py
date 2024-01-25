class State:
  instances = [] # list of pointers to all class instances
  def __init__(self, string = None):
    self.__class__.instances.append(self) # add self-pointer to list of instances of class
    if string is not None:
      attributes = string.split("-")
      tags = [tag[:2] for tag in attributes]
      self.name = attributes[tags.index("NA")][2:]
      self.abb = attributes[tags.index("AB")][2:]
      self.population = int(attributes[tags.index("PO")][2:])
      
    else:
      self.name = ""
      self.abb = ""
      self.delegates = 0
      self.population = 0
    
  def __repr__(self):
    return (
      "ST" +
      "-NA" + str(self.name) +
      "-AB" + str(self.abb) +
      "-PO" + str(self.population)
      )

# how to dynamically change populations... could come up with a function of some kind and have users select a year
# or just set them to a fixed value - 2020 Census

states = [
  "ST-NAAlabama-ABAL-PO5024279",
  "ST-NAAlaska-ABAK-PO733391",
  "ST-NAArizona-ABAZ-PO7151502",
  "ST-NAArkansas-ABAK-PO3011524",
  "ST-NAAmerican Samoa-ABAS-PO46189",
  "ST-NACalifornia-ABCA-PO39538223",
  "ST-NAColorado-ABCO-PO5773714",
  "ST-NAConnecticut-ABCT-PO3605944",
  "ST-NADelaware-ABDE-PO989948",
  "ST-NADistrict Of Columbia-ABDC-PO689545",
  "ST-NAFlorida-ABFL-PO21538187",
  "ST-NAGeorgia-ABGA-PO10711908",
  "ST-NAGuam-ABGU-PO169231",
  "ST-NAHawaii-ABHI-PO1455271", 
  "ST-NAIdaho-ABID-PO1839106",
  "ST-NAIllinois-ABIL-PO12812508",
  "ST-NAIndiana-ABIN-PO6785528",
  "ST-NAIowa-ABIA-PO3190369",
  "ST-NAKansas-ABKS-PO2937880",
  "ST-NAKentucky-ABKY-PO4505836",
  "ST-NALouisiana-ABLA-PO4657757",
  "ST-NAMaine-ABME-PO1362359",
  "ST-NAMaryland-ABMD-PO6177331",
  "ST-NAMassachusetts-ABMA-PO7029917",
  "ST-NAMichigan-ABMI-PO10077331",
  "ST-NAMinnesota-ABMN-PO5706494",
  "ST-NAMississippi-ABMS-PO2961279",
  "ST-NAMissouri-ABMO-PO6154913",
  "ST-NAMontana-ABMT-PO1084225",
  "ST-NANebraska-ABNE-PO1961504",
  "ST-NANevada-ABNV-PO3104614",
  "ST-NANew Hampshire-ABNH-PO1377529",
  "ST-NANew Jersey-ABNJ-PO9288994",
  "ST-NANew Mexico-ABNM-PO2117522",
  "ST-NANew York-ABNY-PO20201249",
  "ST-NANorth Carolina-ABNC-PO10439388",
  "ST-NANorth Dakota-ABND-PO779094",
  "ST-NANorthern Mariana Islands-ABMP-PO49587",
  "ST-NAOhio-ABOH-PO11799448", 
  "ST-NAOklahoma-ABOK-PO3959353",
  "ST-NAOregon-ABOR-PO4237256",
  "ST-NAPennsylvania-ABPA-PO13002700",
  "ST-NAPuerto Rico-ABPR-PO3271564",
  "ST-NARhode Island-ABRI-PO1097379",
  "ST-NASouth Carolina-ABSC-PO5118425",
  "ST-NASouth Dakota-ABSD-PO886667",
  "ST-NATennessee-ABTN-PO6910840",
  "ST-NATexas-ABTX-PO29145505",
  "ST-NAUtah-ABUT-PO3271616",
  "ST-NAVermont-ABVT-PO642077",
  "ST-NAVirginia-ABVA-PO8631393",
  "ST-NAVirgin Islands-ABVI-PO87146",
  "ST-NAWashington-ABWA-PO7705281",
  "ST-NAWest Virginia-ABWV-PO1793716",
  "ST-NAWisconsin-ABWI-PO5893718",
  "ST-NAWyoming-ABWY-PO576851"
]

State.instances = []
[State(state_) for state_ in states]