Add-Type -AssemblyName System.IO.Compression.FileSystem
$docPath = (Get-Item "Proposta_TCC_ABNT_Brenon.docx").FullName
$zip = [System.IO.Compression.ZipFile]::OpenRead($docPath)
$entry = $zip.GetEntry('word/document.xml')
$stream = $entry.Open()
$reader = New-Object System.IO.StreamReader($stream, [System.Text.Encoding]::UTF8)
$xml = [xml]$reader.ReadToEnd()
$reader.Close()
$stream.Close()
$zip.Dispose()
$text = ($xml.document.body.p | ForEach-Object { $_.InnerText.Trim() }) -match '\S'
$text -join "`r`n" | Out-File -FilePath "$PSScriptRoot\abnt_text.txt" -Encoding utf8
