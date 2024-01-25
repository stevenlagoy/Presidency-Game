import sys
import os
import tkinter as tk
from PIL import ImageTk, Image
from tkinter.font import Font
import ctypes as ct
import random as rand
import math as math
import json as json
import time as time
from character import Character
from state import State # generates all states
from primary import Primary
from bloc import Bloc, blocs # generates all blocs

'''how the game works:
  the player will be introduced to other prospective candidates based on their alignments.
  all candidates will be ranked based on some value from the profile roll
  in a turn-based system, the player and candidates will choose another candidate to be their running mate and a party to align with
  only a dozen candidate pairs will be allowed in each party. others will be eliminated from the race
  based on running mates, the remaining candidates will gain their vp's stats
  
  the initial fundraising stage has several rounds:
  candidates will try to get as close as possible to their current amount (initially $10,000) without going over
  there will be three rounds. if a player ends the round, that sum is added to their current amount
  if they go over, they will lose a quarter of their money
  
  they must then assemble their cabinet. cabinet members increase abilities, campaign power, and conviction
  
  ATTRIBUTES:
  Age
    Min is 35, will increase some chances to a certain point, then decrease them
    35<=age<=120
    f(x) = -(((x-55)^2)/15)+100 when age < 75
           -(12x+2000)/15 when age >= 75
    
  Alignment
    Along several axes, will determine later party alignment
    (x,y)
    -100<x<100
    -100<y<100
    random choices are weighted towards the center and y=x axes
    
  Experience
    Can involve other political positions
    Military
    Lawyers
    Governors
    Representatives
    Senators
    Vice Presidents
    Cabinet Members (especially Sec. of State)
    Professors
    Ministers
    Party Leaders
    
  Education
    Level and type of education, can also determine possible experience
    ISCED numbers?
    1 = Primary Education
    2 = Lower Secondary Education
    3 = Upper Secondary Education
    4 = Post-secondary non-Tertiary Education
    5 = Short-cycle tertiary education
    6 = Bachelors degree or equivalent tertiary education level
    7 = Masters degree or equivalent tertiary education level
    8 = Doctoral degree or equivalent tertiary education level
  
  Physical
    how they look (will have to think ethically about this)
  Origin
    Where they are from - boosts popularity in their state
    List of all US states and territories, plus overseas or military children
  Name
    Their name
  Skills
    Their weighted ability scores:
      executive - strong deicisions
        affects speed and charisma
        ethos - about the candidate
      
      legislative - lasting decisions
        affects planning and leverage
        pathos - about the voter
      
      judicial - informed decisions
        affects inspection and reasoning
        logos - about the country
        
      it's a triangle:
        boosting one lowers the two others quickly
        lowering one boosts the two others slowly
        it also has a third dimension, which is based on the total sum of the skills:
        can be the exact center with [0,0,0] or [100,100,100]
        
        the triange for all candidates will be visible, but their total aptitude will be hidden
        in the case of a tie, the candidate with the higher aptitide will often win
        chances vary between aptitudes by y = ((x1-x2)/100)^2,
        where x1 is one character's aptitude and x2 is another
        the character represented by x1 will have that percentage more chance to win as long as
        (-x1+x2)/5000 > 0, and the reverse is true for the other character
  
      stagnation/conviction - drifts to a set value over time, can be increased by events
      campaign power
  
  Order:
  Name
  Age
  State origin
  Education
  Alignment
  Experience
  
  From this is determined the skills
  
  I would like for conviction and politics to be determined from an actual survey about the candidate's positions on some topics...
  but that seems like the surest way to get players cancelled lol
  could come up with fake political arguments? where to draw the line between realism and fun
  Will have to look into the approach that games like Democracy take... real politics but less controversial somehow
  
  The focus of this game is to gain votes through publicity and promises, then maybe seeing how well they can be met?
  The candidacy and presidential race is the main focus, making compromises and balancing plans
  
  Difficulty amount: how you compare to other candidates, how the voterbase is concentrated, how often you have to change plans
  Should be more difficult because of calculated risk, not necessarily because of randomness
  
  Easiest level could be Incumbent - you start as one of the only candidates for your party, can rarely backfire though
  Level 2 - More character roll points, but not incumbent
  Level 3 - Regular difficulty
  Level 4 - Fewer character roll points
  Level 5 - Running against incumbent (may or may not be same party)
  Secret difficulties:
  Level 6 - Fewer roll points, introduces more random bad luck (125% bad luck)
  Level 7 - Running against incumbent of same party
  Level 8 - Very few roll points, 150% bad luck
  Supersecret difficulty:
  Level 16 - Running with virtually no roll points, against others with tripled rolls and luck, with triple the bad luck and even more events
'''

'''events:

events can happen randomly or as a result of some actions
they have a mean time to happen (mtth) which represents the average number of turns it will take for an event to fire
mtth of 0 is instant

events can have no effect, can add or remove player modifiers, etc
some events will have several decisions or actions to be made about them. inaction is also sometimes possible
events can also be chained - they lead to each other
when an event introduces a chance of something ocurring, that chance is seeded and will always have the same result - to prevent savescumming
(perhaps there could be a debug mode which allows different outcomes than the predetermined)
also, players have a cumulative luck value which is determined by how often they have achieved a "favorable" result - lots of good events in a row lower the luck, lots of bad ones raise it
thus, the displayed probability and the actual probability will be different

'''

SAVEDIR = "C:\\Users\\LaGoySM\\Downloads\\Documents\\Presidency Game\\savegames\\" # constant directory of saves folder

''' would like to have a list of all the standard fonts here
class fonts:
  TITLE_FONT = Font(
    family = 'Georgia',
    size = 32,
    weight = 'bold',
  )
'''
  
def get_curr_screen_geometry():
  # find the size (in pixels) of current active screen even when there are multiple monitors
  # returns geometry: Tk geometry string [width]x[height]+[left]+[top]

  root = tk.Tk()
  root.update_idletasks()
  root.attributes('-fullscreen',True)
  root.state('iconic')
  geometry = root.winfo_geometry()
  root.destroy()
  print(geometry)
  return geometry

def dark_title_bar(window):
  # make a window have a dark windows topbar
  # takes an instance of Tk window
  window.update()
  set_window_attribute = ct.windll.dwmapi.DwmSetWindowAttribute
  get_parent = ct.windll.user32.GetParent
  hwnd = get_parent(window.winfo_id())
  value = 2
  value = ct.c_int(value)
  set_window_attribute(hwnd, 20, ct.byref(value), 4)
  # only works sometimes????

def clearWindow(root):
  # clear a Tk window
  for frame in root.winfo_children():
    if frame.widgetName == 'frame':
      frame.pack_forget()

def on_closing():
  sys.exit(0)

def createWindow():
  root = tk.Tk()
  root.title("Race for the Presidency")
  root.state("zoomed")
  root.configure(background="#101010")
  dark_title_bar(root)
  root.iconbitmap("icon.ico")
  root.resizable(False, False)

  # create all the frames - this might be moved elsewhere later

  # menu frame
  menu_frame = tk.Frame(root)

  title_text = tk.Label(menu_frame, text = "Race for the Presidency", font = Font(family = 'Georgia', size = 32, weight = 'bold'))
  title_text.pack()

  new_save_button = tk.Button(menu_frame, text = "New Game", command = lambda: openFrame(root, new_game_frame))
  new_save_button.pack()

  continue_button = tk.Button(menu_frame, text = "Continue", command = lambda: openFrame(root, open_save_frame))
  continue_button.pack()

  tutorial_button = tk.Button(menu_frame, text = "Tutorial")
  tutorial_button.pack()

  about_button = tk.Button(menu_frame, text = "About")
  about_button.pack()

  close_game_button = tk.Button(menu_frame, text = "Close Game", command = lambda: root.destroy())
  close_game_button.pack()
  
  # open save frame
  open_save_frame = tk.Frame(root)

  save_info_label = tk.Label(open_save_frame, text = "Save Name:\nSave Date:", width = 25, height = 2, anchor = "w", justify = "left")
  save_info_label.pack()

  saves_list = tk.Listbox(open_save_frame)
  count = 1
  for file in os.listdir(SAVEDIR):
    saves_list.insert(count, str(file).split(".")[0]) # insert the name of the files (minus the extension) at the next open index
  
  # this has to be a lambda i think... there might be a better way
  # get some info about the file and set the label's text accordingly - i hate to call the function twice but i'm not sure how to use the returned values otherwise
  saves_list.bind('<<ListboxSelect>>', lambda x: save_info_label.config(text =
    "Save Name: " + get_save_info(SAVEDIR + saves_list.get(saves_list.curselection()) + ".txt")[0] +
    "\nSave Date: " + get_save_info(SAVEDIR + saves_list.get(saves_list.curselection()) + ".txt")[1]
    ))
  saves_list.pack()

  open_save_button = tk.Button(open_save_frame, text = "Open Save", command = lambda: open_save(SAVEDIR + saves_list.get(saves_list.curselection()) + ".txt"))
  open_save_button.pack()

  saves_list_back_button = tk.Button(open_save_frame, text = "Back", command = lambda: openFrame(root, menu_frame))
  saves_list_back_button.pack()

  # new game frame
  new_game_frame = tk.Frame(root)

  new_game_back_button = tk.Button(new_game_frame, text = "Back", command = lambda: openFrame(root, menu_frame))
  new_game_back_button.pack()

  start_game_button = tk.Button(new_game_frame, text = "Announce Candidacy", command = lambda: openFrame(root, character_view_frame))
  start_game_button.pack()

  # character view frame
  character_view_frame = tk.Frame(root)

  image1 = Image.open("empty_portrait.png")
  test = ImageTk.PhotoImage(image1)
  portrait_label = tk.Label(character_view_frame, image = test)
  portrait_label.image = test
  portrait_label.place(x=100, y=100)
  portrait_label.pack()


  settings_button = tk.Button(character_view_frame, text = "Settings")
  settings_button.pack()

  # party view frame
  party_view_frame = tk.Frame(root)

  # geography view frame
  geography_view_frame = tk.Frame(root)

  # blocs view frame
  blocs_view_frame = tk.Frame(root)

  # set up window
  openFrame(root, menu_frame)

  root.protocol("WM_DELETE_WINDOW", on_closing) # stops the whole program when the tk window is closed
  root.mainloop()

  return root

def openFrame(root, frame_to_open):
  # open the menu frame
  # takes the active Tk window
  clearWindow(root)
  frame_to_open.pack()

def rollDice(numDice, sides = 6):
  diceTotal = 0
  for i in range(numDice):
    diceTotal += rand.randint(1,sides)
  return diceTotal

def get_save_info(path):
  # returns a small amount of information about a save game
  # [name, date]

  file = open(path, "r") # open save file in read mode
  return [file.readline()[:-1], file.readline()[:-1]]

def save(savename):
  # append the current game to "saves.txt"
  # takes the name of the save as a string
  '''need to save:
    characters
    states?
    primaries?
    event history
  
  saves should be titled the same as the player character's name, plus the date-time it was saved at
  the save screen should have a checkbox to remove old saves, checking it will remove saves other than the three backups
  the name and date of the save is also included within the file itself, incase the filename gets changed somehow
  '''

  file = open(SAVEDIR + savename, "a") # create save file in append mode
  file.write("SAVE" + str(savename) + "\n") # write the name of the save
  file.write("TIME" + str(int(time.time())) + "\n") # write the time when saved
  file.write("PLAY" + str([char.name for char in Character.instances if char.is_player is True][0]) + "\n") # write the name of the character
  for character in Character.instances: # write the rest of the characters
    file.write(character.__repr__() + "\n")
  file.write("\n") # end line
  file.close() # close the file
  # this should eventually be encrypted and scrambled
  # could make a unique file extension to keep the save files in - requires OS instuctions for opening file

def open_save(path):
  # opens a single save
  # takes the path to a single file
  save_data = ""
  try:
    file = open(path, "r") # open file in read mode
    for line in file.readlines():
      save_data += line if line != "\n" else "" # sort out blank lines
    file.close() # close the file
  except FileNotFoundError:
    pass

  print(save_data)

  # this is where the complicated part goes where it creates all the characters and states and everything based on what's in the file

def reset():
  Character.instances = []

def main():

  reset()

  window = createWindow()
  
  reset()

  print("Finished")

if __name__ == "__main__":
  main()