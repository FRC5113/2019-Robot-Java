def getConfig(path):
    configFile = open(path)
    configText = configFile.read()
    configFile.close()
    config = eval(configText)
    return config
