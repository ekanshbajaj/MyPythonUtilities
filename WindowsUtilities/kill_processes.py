import psutil

for proc in psutil.process_iter(['name']):
    try:
        # Check if the process name contains "chromedriver"
        if "chromedriver" in proc.info['name'].lower():
            proc.kill()
    except (psutil.NoSuchProcess, psutil.AccessDenied, psutil.ZombieProcess):
        pass