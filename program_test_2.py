import tkinter as tk
from PIL import Image, ImageTk

class RootWindow(tk.Tk):
    def __init__(self):
        tk.Tk.__init__(self)
        self.title("Fullscreen Application")
        
        # Make the window fullscreen
        self.attributes('-fullscreen', True)
        
        # Load and resize the background image
        image = Image.open("gfx/loadscreens/inaguration.png")  # Replace "background_image.jpg" with your image file
        image = image.resize((self.winfo_screenwidth(), self.winfo_screenheight()), Image.LANCZOS)
        self.background_photo = ImageTk.PhotoImage(image)
        
        # Create a container to hold all frames
        container = tk.Frame(self)
        container.pack(side="top", fill="both", expand=True)
        container.grid_rowconfigure(0, weight=1)
        container.grid_columnconfigure(0, weight=1)
        
        # Dictionary to hold frames
        self.frames = {}
        
        # Create frames and add them to the dictionary
        for F in (Frame1, Frame2):
            frame = F(container, self)
            self.frames[F] = frame
            frame.grid(row=0, column=0, sticky="nsew")
        
        self.show_frame(Frame1)
    
    def show_frame(self, cont):
        frame = self.frames[cont]
        frame.tkraise()

class Frame1(tk.Frame):
    def __init__(self, parent, controller):
        tk.Frame.__init__(self, parent)
        
        # Create a label to display the background image
        background_label = tk.Label(self, image=controller.background_photo)
        background_label.place(x=0, y=0, relwidth=1, relheight=1)  # Fill the entire frame
        
        label = tk.Label(self, text="Frame 1", font=("Arial", 18))
        label.pack(pady=10, padx=10)
        
        button = tk.Button(self, text="Go to Frame 2",
                           command=lambda: controller.show_frame(Frame2))
        button.pack()

class Frame2(tk.Frame):
    def __init__(self, parent, controller):
        tk.Frame.__init__(self, parent)
        
        label = tk.Label(self, text="Frame 2", font=("Arial", 18))
        label.pack(pady=10, padx=10)
        
        button = tk.Button(self, text="Go to Frame 1",
                           command=lambda: controller.show_frame(Frame1))
        button.pack()

if __name__ == "__main__":
    app = RootWindow()
    app.mainloop()
