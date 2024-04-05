import tkinter as tk
from PIL import Image, ImageTk

class RootWindow(tk.Tk):
    def __init__(self):
        tk.Tk.__init__(self)
        self.title("Fullscreen Application")
        
        # Make the window fullscreen
        self.attributes('-fullscreen', True)
        
        # Create a container to hold all frames
        container = tk.Frame(self, width = 2550, height = 1440)
        container.pack(side="top", fill="both", expand=True)
        
        # Dictionary to hold frames
        self.frames = {}        
        
        # Create frames and add them to the dictionary
        for F in (StartPage, Frame1, Frame2):
            frame = F(container, self)
            self.frames[F] = frame
            frame.grid(row=0, column=0, sticky="nsew")
        
        self.show_frame(StartPage)

    def show_frame(self, cont):
        frame = self.frames[cont]
        frame.tkraise()

class StartPage(tk.Frame):
    def __init__(self, parent, controller):
        tk.Frame.__init__(self, parent, width=2550, height=1440)
        
        # Create a label to display the background image
        image = Image.open("gfx/loadscreens/inaguration.png")
        image = image.resize((self.winfo_screenwidth(), self.winfo_screenheight()), Image.LANCZOS)
        self.background_photo = ImageTk.PhotoImage(image)
        self.background_label = tk.Label(self, image=self.background_photo)
        self.background_label.place(x=0, y=0, relwidth=1, relheight=1)  # Fill the entire frame
        
        label = tk.Label(self, text="Start Page", font=("Arial", 18))
        label.pack(pady=10, padx=10)
        
        button1 = tk.Button(self, text="Go to Frame 1",
                            command=lambda: controller.show_frame(Frame1))
        button1.pack()
        
        button2 = tk.Button(self, text="Go to Frame 2",
                            command=lambda: controller.show_frame(Frame2))
        button2.pack()

    def hide_background(self):
        self.background_label.place_forget()

    def show_background(self):
        self.background_label.place(x=0, y=0, relwidth=1, relheight=1)

class Frame1(tk.Frame):
    def __init__(self, parent, controller):
        tk.Frame.__init__(self, parent)
        label = tk.Label(self, text="Frame 1", font=("Arial", 18))
        label.pack(pady=10, padx=10)
        
        button = tk.Button(self, text="Back to Home",
                           command=lambda: controller.show_frame(StartPage))
        button.pack()

class Frame2(tk.Frame):
    def __init__(self, parent, controller):
        tk.Frame.__init__(self, parent)
        label = tk.Label(self, text="Frame 2", font=("Arial", 18))
        label.pack(pady=10, padx=10)
        
        button = tk.Button(self, text="Back to Home",
                           command=lambda: controller.show_frame(StartPage))
        button.pack()

if __name__ == "__main__":
    app = RootWindow()
    app.mainloop()
