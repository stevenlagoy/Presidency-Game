class Issue:
    instances = []
    def __init__(self, name, desc = "", levels = []):
        self.__class__.instances.append(self)
                
        self.name = name
        self.description = desc
        self.levels = levels



'''
Issues in the US in 2020

Economy
        Social Security
                Retirement Age (make a continuum by year)
                        "Proposals to adjust the retirement age for Social Security benefits can be contentious. Some argue for increasing the retirement age to address demographic shifts and increased life expectancy, while others express concerns about the impact on individuals who may face challenges working later in life."
                        55
                        57
                        60
                        62
                        65 -- previous
                        67 -- current
                        70
                COLA
                        "Adjustments to Social Security benefits to account for the cost of living are often debated. The method used to calculate these adjustments and the frequency of updates are key considerations."
                Disability
                        "Discussions may center on the eligibility criteria and administration of Social Security Disability Insurance (SSDI) benefits."

        Medicare
        Tax Rates
        Infrastructure
                "Infrastructure policies in the United States involve planning, funding, and implementing projects related to transportation, energy, water systems, broadband, and other essential components of the nation's physical and technological framework. Infrastructure has been a recurring focus in political discussions due to the importance of maintaining and modernizing critical systems."
                Investment and funding
                Bipartisan Cooperation
                Project Prioritization
                Climate Resilience
                Technological Innovation
                Private Partnerships
                Job Creation
        Debt
                Deficit Spending
                        "Debates surrounding the national debt often touch on the concept of deficit spending, where the government intentionally incurs budget deficits during economic downturns to stimulate economic activity. Critics argue that sustained deficit spending without corresponding economic growth can contribute to the growth of the national debt."
                Interest Payment
                        "The amount of money spent on interest payments on the national debt is a significant consideration. As the debt grows, so does the cost of servicing it, potentially leading to a situation where a substantial portion of the federal budget is allocated to paying interest."
                Debt Ceiling
                        "The debt ceiling is a legal limit set by Congress on the amount of debt the federal government can incur. Periodically, there are political debates about whether to raise the debt ceiling to allow the government to meet its financial obligations. Failure to raise the debt ceiling could lead to a government shutdown or default on existing obligations."
        Government Efficiency
        Defense Spending
                liquidation
                0%
                3%
                6%
                9%
                12% of federal spending (3.5% GDP) 877 Billion -- current
                15%
        Inheritence Tax
                0%
                10%
                20%
                30%
                40% -- current
                50%
Terrorism
Foreign Policy
        Threats
        Allied Defense
        NATO
Health Care
        Affordable Care Act

Gun Policy
        "Discussions often revolved around proposals for stricter gun control measures, including background checks for all gun buyers, closing loopholes in existing laws, and implementing measures to prevent individuals with a history of violence or mental illness from obtaining firearms."
                No limits
                Restricted Military-grade weapons
                Automatic weapons ban
                Licencing for all firearms
                Minimum age for licence
                Very restricted gun ownership
                Complete firearm ban
        Restriction to those who would cause harm
        Background Checks / Red-Flag Laws
                "Red flag laws, which allow the temporary removal of firearms from individuals deemed to be a risk to themselves or others, were part of the conversation. Some advocated for the adoption of red flag laws at the federal level or in states where they were not already in place."
                "Calls for universal background checks on all gun sales, including private transactions and at gun shows, were part of the ongoing dialogue. Supporters argued that this measure could help prevent firearms from falling into the wrong hands."
        Guns in Schools
        Manufacturer Legal Immunity
        Concealed Carry
                "Discussions included debates about concealed carry laws, reciprocity between states, and whether individuals should be allowed to carry concealed weapons in public places."
        Violence Prevention Programs
                "Some discussions focused on the need for comprehensive gun violence prevention programs, including community-based initiatives, mental health support, and strategies to address the root causes of violence."
                
Crime
        Marijuana Legalization
                Legal
                Illegal decriminalized
                Illegal unenforced
                Illegal
                
                Medical
                Recreational

Immigration
        Refugees / Asylum
        Undocumented Immigrants
        Deportation
        Family Separation
        Visas
        Job Availability
        Allowing Criminals to enter / be tried in US
        ICE Detention
        Border Wall
        Border Control (Military)
        Birthright Citizenship
        Counter-Cartel Military Operation
        Ban from Muslim-Majority countries

Education
        Student Debt
        Systemic Racism Education
        Sexual Education
        Transgender Students
        Sports Integration
        Gender-Affirming Care

Treatment of Minorities

Trade Policy
        "The United States has historically pursued a combination of free trade and protectionist measures, depending on the prevailing political ideology. Key elements of U.S. trade policy include promoting fair trade practices, protecting domestic industries, and seeking market access for American goods and services abroad."
        
        Market Undercutting
                "The U.S.-China trade relationship is a major focal point. Trade tensions have been prominent, with issues such as intellectual property theft, market access, and a trade imbalance being central concerns. Tariffs and trade restrictions have been employed as tools in negotiating a more favorable balance."
                Aggressive Enforcement
                Intellectual property protection
        
        Environmental Considerations:
                "Climate change and environmental concerns have gained prominence, with discussions about incorporating environmental standards into trade agreements to address sustainability and carbon emissions."
                Climate (carbon) sanctions
                Clean Air and Water protections
                Paris Climate Agreement

        Trade union support
                "Trade Unions advocate for the interests of workers and seek to influence policies that impact employment, wages, and working conditions. The relationship between trade unions and trade policy is multifaceted and can vary depending on the specific policies in question and the broader economic and political climate."

        Sustainable growth
                "The approach to trade often varies between Democratic and Republican administrations. Democrats may focus on labor and environmental standards, while Republicans may prioritize reducing regulatory barriers and promoting business interests."

        Tariffs
                "The imposition of tariffs, particularly against China, has been a notable feature of recent U.S. trade policy. Trade tensions and retaliatory measures have sparked concerns about potential trade wars, affecting global economic stability."
        
        Global Alliances
                "The U.S. is a member of various trade agreements and organizations, including the World Trade Organization (WTO), North American Free Trade Agreement (NAFTA), and more recently, the United States-Mexico-Canada Agreement (USMCA)."
                Reliance on national trading partners (globalism)
                WTO
                NAFTA
                USMCA
                Trans-Pacific Partnership
                
        Job Protection
                "Domestic job protection is often a key political driver, with policymakers aiming to balance the interests of American workers and businesses when formulating trade policies."
                Bailouts to farmers and businesses
                Job outsourcing

Climate Change
        Belief in threat
        Belief in human causes
        Clean Energy
        EV Subsidies
        Market Force / Regulation
                Rely on the market to solve problems
                        "There's an increasing focus on the role of businesses and financial institutions in addressing climate change. Discussions include sustainable business practices, disclosure of climate-related risks, and the integration of environmental, social, and governance (ESG) factors in investment decisions."
                Rely on regulations to force solutions

Abortion
        Gestational limits (continuum)
                6 weeks
                12 weeks (before 2nd trimester)
                15 weeks
                18 weeks
                20 weeks
                22 weeks
                24 weeks (fetal viability)
                27 weeks (before 3rd trimester)
        Risk to mother
                risk to life
                risk to health
                fetal impairment
                nonconsentual / incestual
                socioeconomic factors
        Legal standard for health/life
                Reasonable medical judgement
                Good faith clinical judgement
                Reasonable cause
        Illegality
Treatment of LGBT+


https://www.washingtonpost.com/politics/interactive/2023/presidential-candidates-2024-policies-issues/

'''

# to be honest i have no clue how to group these - many issues contain each other
issues = [
        {
                "name" : "Retirement Age",
                "desc" : ""
        }
]