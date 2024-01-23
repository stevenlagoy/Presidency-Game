class State:
  instances = [] # list of pointers to all class instances
  def __init__(self, string = None):
    self.__class__.instances.append(self) # add self-pointer to list of instances of class
    if string is not None:
      attributes = string.split("-")
      tags = [tag[:2] for tag in attributes]
      self.name = attributes[tags.index("NA")][2:]
      self.abb = attributes[tags.index("AB")][2:]
      self.delegates = int(attributes[tags.index("DE")][2:])
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
      "-DE" + str(self.delegates) +
      "-PO" + str(self.population)
      )

# how to dynamically change populations... could come up with a function of some kind and have users select a year
# or just set them to a fixed value - 2020 Census

states = [
  'ST-NAAlabama-ABAL-DE9-PO5024279',
  'ST-NAAlaska-ABAK-DE3-PO733391',
  'ST-NAArizona-ABAZ-DE11-PO7151502',
  'ST-NAArkansas-ABAK-DE6-PO3011524',
  'ST-NAAmerican Samoa-ABAS-DE0-PO46189',
  'ST-NACalifornia-ABCA-DE54-PO39538223',
  'ST-NAColorado-ABCO-DE10-PO5773714',
  'ST-NAConnecticut-ABCT-DE7-PO3605944',
  'ST-NADelaware-ABDE-DE3-PO989948',
  'ST-NADistrict Of Columbia-ABDC-DE3-PO689545',
  'ST-NAFlorida-ABFL-DE30-PO21538187',
  'ST-NAGeorgia-ABGA-DE16-PO10711908',
  'ST-NAGuam-ABGU-DE0-PO169231',
  'ST-NAHawaii-ABHI-DE4-PO1455271',
  'ST-NAIdaho-ABID-DE4-PO1839106',
  'ST-NAIllinois-ABIL-DE19-PO12812508',
  'ST-NAIndiana-ABIN-DE11-PO6785528',
  'ST-NAIowa-ABIA-DE6-PO3190369',
  'ST-NAKansas-ABKS-DE6-PO2937880',
  'ST-NAKentucky-ABKY-DE8-PO4505836',
  'ST-NALouisiana-ABLA-DE8-PO4657757',
  'ST-NAMaine-ABME-DE4-PO1362359',
  'ST-NAMaryland-ABMD-DE10-PO6177331',
  'ST-NAMassachusetts-ABMA-DE11-PO7029917',
  'ST-NAMichigan-ABMI-DE15-PO10077331',
  'ST-NAMinnesota-ABMN-DE10-PO5706494',
  'ST-NAMississippi-ABMS-DE6-PO2961279',
  'ST-NAMissouri-ABMO-DE10-PO6154913',
  'ST-NAMontana-ABMT-DE4-PO1084225',
  'ST-NANebraska-ABNE-DE5-PO1961504',
  'ST-NANevada-ABNV-DE6-PO3104614',
  'ST-NANew Hampshire-ABNH-DE4-PO1377529',
  'ST-NANew Jersey-ABNJ-DE14-PO9288994',
  'ST-NANew Mexico-ABNM-DE5-PO2117522',
  'ST-NANew York-ABNY-DE28-PO20201249',
  'ST-NANorth Carolina-ABNC-DE16-PO10439388',
  'ST-NANorth Dakota-ABND-DE3-PO779094',
  'ST-NANorthern Mariana Islands-ABMP-DE0-PO49587',
  'ST-NAOhio-ABOH-DE17-PO11799448',
  'ST-NAOklahoma-ABOK-DE7-PO3959353',
  'ST-NAOregon-ABOR-DE8-PO4237256',
  'ST-NAPennsylvania-ABPA-DE19-PO13002700',
  'ST-NAPuerto Rico-ABPR-DE0-PO3271564',
  'ST-NARhode Island-ABRI-DE4-PO1097379',
  'ST-NASouth Carolina-ABSC-DE9-PO5118425',
  'ST-NASouth Dakota-ABSD-DE3-PO886667',
  'ST-NATennessee-ABTN-DE11-PO6910840',
  'ST-NATexas-ABTX-DE40-PO29145505',
  'ST-NAUtah-ABUT-DE6-PO3271616',
  'ST-NAVermont-ABVT-DE3-PO642077',
  'ST-NAVirginia-ABVA-DE13-PO8631393',
  'ST-NAVirgin Islands-ABVI-DE0-PO87146',
  'ST-NAWashington-ABWA-DE12-PO7705281',
  'ST-NAWest Virginia-ABWV-DE4-PO1793716',
  'ST-NAWisconsin-ABWI-DE10-PO5893718',
  'ST-NAWyoming-ABWY-DE3-PO576851'
  ]

[State(state_) for state_ in states]