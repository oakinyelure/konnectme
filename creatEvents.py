import requests, sqlite3, time

try:
    KMDB
except NameError:
    #Create database
    KMDB = sqlite3.connect("testing.db")
    cur = KMDB.cursor()
    #Create table
    cur.execute('''CREATE TABLE events (event_name,event_time,location,event_coordinate,timestamp);''')

# URL from KonnectMe
KM_url = "https://api.github.com/user/repos"
r = requests.get(KM_url).json()
r[unicode('timestamp')] = unicode(time.time())
        
# Insert a row of data (Trying to give the same name into the html for each events info)
cur.execute("INSERT INTO events VALUES (r['event_name'],r['event_time'],r['location'],r['event_coordinate'],r['timestamp'])")
# Save the changes
cur.commit()
cur.close()