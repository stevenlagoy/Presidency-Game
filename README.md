# Race for the Presidency

A real-time strategy/roleplaying game that simulates the complex journey of running for President of the United States.

## NOTE

This is a work-in-progress personal project. While running this code will not harm your computer, I cannot guarantee it will run on every machine, and even if it does run then there's likely little to be seen. The game is very much in pre-alpha and everything is liable to change with later development. So have no expectations for what this game will actually do. Thanks :) ✨

## Overview

Race for the Presidency is a detailed political simulation game where you take on the role of a presidential candidate in the United States. Navigate the complex political landscape as you:

- Create and customize your candidate with unique traits, skills, and political positions
- Balance resources including funds, influence, and pledges
- Appeal to diverse demographic voting blocs across the country
- Respond to real-time events that can make or break your campaign
- Manage relationships with party members, other candidates, and your campaign staff
- Win your party's nomination through primaries and caucuses
- Ultimately compete to win the Electoral College and become President

## Features

- **Deep Character Customization**: Create a unique candidate with customizable demographics, skills, experience, education, and political positions.
- **Geographic Detail**: Campaign across states, congressional districts, and counties, each with their own demographic compositions.
- **Diverse Voting Blocs**: Appeal to voters across various demographics including age, religion, education, ethnicity, and more.
- **Resource Management**: Balance campaign funds, personal influence, and supporter pledges.
- **Political Positioning**: Take stances on important issues that may appeal to some voters while alienating others.
- **Dynamic Events**: React to unexpected events that can change the course of your campaign.
- **AI Opponents**: Compete against computer-controlled candidates with their own strategies and personalities.
- **Multiple Difficulty Levels**: Choose your challenge, from running as an incumbent to facing steep opposition.

## Installation

### Requirements
- Java 11 or higher
- Maven 4.0.0
- At least 4GB RAM recommended
- 500MB free disk space

### Setup Instructions

1. Clone the repository:
   ```
   git clone https://github.com/yourusername/race-for-presidency.git
   ```

2. Navigate to the project directory:
   ```
   cd race-for-presidency
   ```

3. Compile the project:
   ```
   mvn clean package
   ```

4. Run the game:
   ```
   java -jar build/libs/race-for-presidency.jar
   ```
   
   Alternatively, you can run directly from the source:
   ```
   java -cp build/classes/java/main core.Main
   ```

## How to Play

1. **Character Creation**: Start by customizing your candidate, choosing demographics, background, education, and political leanings.

2. **Early Campaign**: Begin by building name recognition, raising funds, and establishing your platform.

3. **Primary Season**: Compete against other candidates in your party through state primaries and caucuses.

4. **Party Convention**: Secure your party's nomination at the national convention.

5. **General Election**: Campaign across the country, participate in debates, and work to win the Electoral College.

6. **Election Day**: Watch the results come in state by state and see if your strategy paid off.

## Game Structure

The game is organized around several key phases:

- **Pre-Campaign Phase**: Build your candidate and initial support
- **Primary Phase**: Win your party's nomination
- **General Election Phase**: Campaign against the opposing party's candidate
- **Election Day**: The final results
- **Post-Election**: If you win, prepare for inauguration

## Credits

Developed by Steven LaGoy
2022-2025

## Contact

For bug reports or feature requests, please open an issue on the GitHub repository, or contact Steven LaGoy at stevenlagoy@gmail.com

---

*Race for the Presidency is a work of fiction. Any resemblance to real persons or events is for simulation purposes only and is not intended to reflect the real actions or views of any persons involved.*
