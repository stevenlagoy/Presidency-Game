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