d:
cd D:\IntelliJ\Proyectos\ControlDePedidos\
del ControlDePedidos.zip
powershell Compress-Archive -Path "runme.cmd", "runWithLogs.cmd", "target\ControlDePedidos*.jar", "target\lib", "target\config" -DestinationPath "ControlDePedidos.zip"
