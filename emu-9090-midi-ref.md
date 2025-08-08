# GENERAL INFORMATION FOR ORBIT SYSEX

* Product ID for Orbit is 0A.
* Device ID is [00-0F] (0-15 decimal).
* Parameter Number and Parameter Value are 2 bytes each.
* Since MIDI data bytes cannot be greater than [7F] (127 decimal), the data values are “nibble-ized” to a 14-bit signed 2's complement format.
* There is only one edit buffer which is for the current preset (the preset shown in the display). Only one preset at a time can be edited via SysEx commands and changing the current preset erases the edit buffer.

## RECEIVED CHANNEL COMMANDS

Channels number (n) = 0-15.
Message bytes are represented in hex. All other numbers are decimal. Running Status is supported.

|Command| Message |Comments|
|-------|---------|--------|
|Note Off| 8n kk vv |release velocity is ignored|
|Note On |9n kk vv |velocity 0 = note off|
|Key Aftertouch |An kk vv |kk = 0-127 vv = 0-127|
|Program Change |Cn vv |vv =0-127|
|Channel Aftertouch| Dn vv |vv = 0-127|
|Pitch Bend |En ll mm |l = lsb, m = msb|
|Realtime Controller| Bn cc vv |cc = 00-31|
|Footswitch| Bn cc vv |cc = 64-79, vv ≥ 64 = on|
|Volume| Bn 07 vv vv= |0-127|
|Pan |Bn 0A vv |0=left, 127=right, 64=center|
|All Sound Off |Bn 78 |00 turns all sound off|
|Reset All Controllers| Bn 79 00 |ignored in omni mode|
|All Notes Off| Bn 7B 00 |ignored in omni mode|
|Omni Mode Off*| Bn 7C 00 |forces all notes & controls off|
|Omni Mode On*| Bn 7D 00 |forces all notes & controls off|
|Mono Mode On (Poly Off)*| Bn 7E 00| forces all notes & controls off|
|Poly Mode On (Mono Off)*| Bn 7F 00| forces all notes & controls off|
|Bank Select| Bn 00 00 20 bb| bb = bank #|

### Special Notes

* From Omni Mode ... Omni Off turns Poly On.
* From Poly Mode ..... Omni On turns Omni On; Mono On turns Mono On.
* From Mono Mode ... Mono Off turns Poly On; Omni On turns Omni On.
* From Multi Mode ... Omni On turns Omni On; Omni Off or Mono Off turns Poly On; Mono On turns Mono On.
* All other changes have no effect.

### MIDI SYSEX COMMANDS

For system exclusive commands, the following format is used:

```
F0 system exclusive status byte
18 E-mu ID byte
0A product ID byte (will also respond to ID 04 - Proteus)
dd device ID byte
cc command byte
... data bytes
F7 EOX
```

### SysEx Editing

Preset and setup parameters may be edited individually using system exclusive commands. The preset being edited is the active preset (the preset on the basic or global channel and the one which is shown in the LCD). The value of a given parameter may be changed by sending a parameter value command. The value of a parameter may be read by sending a parameter value request, to which the machine will respond by sending back the parameter value.

Two MIDI bytes (lsb, msb) are required for each 14 bit data word. Bits0-6 are sent first, followed by bits 7-13 in the next MIDI byte. All data words are signed 2's complement values with sign-extension out to the most significant bit (bit 13). This convention applies to all data words, regardless of the parameter's value range.

Preset data may also be transmitted or received in a single block (one complete preset) using system exclusive commands. A preset data request may be issued by a host computer, to which the machine will respond sending the data block for the requested preset. Conversely, the computer may send new preset data which will replace the specified preset currently in the machine. Additionally, a front panel command will transmit one or all user presets for backup onto an external sequencer. These presets may be restored by simply playing back the
sequence into the machine.

Warning: When transferring preset banks and tuning table data back and forth from Orbit to a computer, the data should be recorded as you would a regular sequence. Sending the data in one huge chunk will clog the input buffer on Orbit unless a time period of approximately 100 mS is inserted between each preset.

RECEIVED SYSTEM EXCLUSIVE COMMANDS

|Command|Message|Comments|
|-------|-------|--------|
|Preset Data Request|F0 18 0A dd 00 ll mm F7|ll= preset # lsb see note 6 mm = msb|
|Preset Data|F0 18 0A dd 01 ll mm … ... cs F7|cs=checksum|
|Parameter Value|F0 18 0A dd 02 pl pm F7|Request pl = parameter # lsb pm = msb|
|Parameter Value|F0 18 0A dd 03 pl pm vl vm F7|pl = parameter # lsb pm = msb vl = value lsb vm = msb|
|Tuning Table Request|F0 18 0A dd 04 F7|see note 7|
|Tuning Table|F0 18 0A dd 05 ... ... F7|262 bytes|
|Program Map Request|F0 18 0A dd 06 F7|see note 8|
|Program Map Data|F0 18 0A dd 07 ... ... F7|262 bytes|
|Master Setting Request|F0 18 0A dd 08 F7|
|Version Request|F0 18 0A dd 0A F7|see note 1|
|Configuration Request|F0 18 0A dd 0C F7|see note 2|
|Instrument List Request|F0 18 0A dd 0E F7|see note 3|
|Preset List Request|F0 18 0A dd 12 F7|see note 4|
|MMA Tuning Dump|F0 7E dd 08 01 tt <name (16 ascii)> … F7|see note 5|
|User Beat Data Request|F0 18 0A dd 30 ll mm F7|ll= beat # lsb mm = msb|
|Song Beat Data Request|F0 18 0A dd 32 ll mm F7|ll= beat # lsb mm = msb|

TRANSMITTED SYSTEM EXCLUSIVE COMMANDS

|Command|Message| Comments|
|-------|-------|---------|
|Preset Data| F0 18 0A dd 01 ll mm ... ... CS F7|ll= preset # lsb mm = msb cs = checksum|
|Parameter Value|F0 18 0A dd 03 pl pm vl vm F7|pl = parameter # lsb pm = msb vl = value lsb vm = msb|
|Tuning Table| F0 18 0A dd 05 ... ... F7|TT data = 256 bytes|
|Program Map Data|F0 18 0A dd 07 ... ... F7|see note 8|
|Config. Message |F0 18 0A dd 0D pl pm s1 l1 m1 s2 l2 m2 F7|see note 2|
|Instr. List| F0 18 0A dd 0F (14 bytes per instr.) ... F7|see note 3|
|Preset List|F0 18 0A dd 13 (13 bytes per preset) ... F7|see note 4|
|Set User Beat Data|F0 18 0A dd 31 ll mm ll mm ll mm ll mm F7|1st ll mm pair = beat # 0-99 2nd pair = tempo 0-240 3rd pair = X factor -36 to +36 4th pair = preset # 0-639|
|Set User Song Data|F0 18 0A dd 33 ll mm… F7|80 bytesll, mm Song Beat # (0-27) see note 11|

### Note 1 - Version Request

This command allows identification of machine type and software revision. Orbit will respond to the request with the version data:

```text
F0 18 0A dd 0B 02 r1 r2 r3 F7
```

r1, r2, r3 = software revision # in ascii (decimal point between r1 & r2).

### Note 2 - Configuration Message

This MIDI command is used to identify the sound sets in a given
Orbit. The configuration request command is:

```hex
F0 18 0A dd 0C F7
```

Orbit will respond to this command with the configuration message:

```hex
F0 18 0A dd 0D pl pm 0B l1 m1 0C l2 m2 F7
```

where:
  * pl and pm are the lsb and msb of the total number of presets, 
  * s1and s2 are the ID numbers of the sound sets contained in this unit,
  * n1=l1, m1 and n2=l2, m2 represent the lsb and msb of the number of instruments in each sound set. If no expansion set is present, s2 will be 7F and n2 will be zero. 

### Orbit Sound Set IDs = 11 & 12 (0B & 0C - hex)

### Note 3 - Instrument List
This MIDI command allows external software to upload the instrument list as an array of ASCII strings. The instrument list request
command is:

```hex
F0 18 0A dd 0E F7
```

Orbit will respond to this command with the instrument list message:

```hex
F0 18 0A dd 0F (14 bytes per instrument) ... ... F7
```

The instruments are transmitted in the same order they appear to the user on Orbit. Note that a given instrument’s position in this list may be different from its actual number within the sound set.

instrument entry: il im (11 ascii bytes) 00

Each instrument entry in the list consists of the actual instrument number (as defined in “Sound Sets” - note 9) in lsb, msb format, followed by the instrument name (11 ascii characters plus a zero terminator) for a total of 14 (decimal) bytes. The first instrument is #1 as displayed on Orbit.
The total number of instrument names is equal to (n1+n2) in the configuration message above. Note that there are less than 255 instruments in the first sound set, therefore there will be a ‘hole’ in the instrument numbering.

### Note 4 - Preset List

This MIDI command allows external software to upload all preset
names as an array of ASCII strings. The preset list request command is:

    F0 18 0A dd 12 F7
Orbit will respond to this command with the preset list message:

    F0 18 0A dd 13 (13 bytes per preset) ... ... F7
Each preset name is 12 ascii characters, plus a zero terminator, for a total of 13 (decimal) bytes. The first preset is #0. The total number of preset names is equal to pp in the configuration message above.

### Note 5 - Bulk Tuning Dump
Orbit can receive MIDI Tuning Standard dumps in addition to its own SysEx tuning table dumps. Orbit will only transmit in it's own SysEx tuning format. The MIDI Tuning Standard is as follows:

    F0 7E dd 08 01 tt <tuning name (16 ascii)> … F7
  - dd= device ID 
  - tt= tuning prog # (ignored) tuning name =(ignored)
  - … = data (xx yy zz) frequency data for one note repeated 128x
  - xx yy zz = 0xxxxxxx 0abcdefg 0hijklmn
  - xxxxxxx = semitone abcdefghijklmn = fraction of semitone in .0061 cent units. 
  - Examples: 
    - Middle C = 3C 00 00 
    - A-440 = 45 00 00

### Note 6 - Preset Data Request
Orbit presets are organized into ranks. Each rank consists of 64 presets.
Orbit has ten ranks of presets (0-639). Ranks may be requested using the preset request command and the appropriate preset code listed below.
|Rank| Preset Range| Preset Code|MIDI Message|
|----|------------|------------|-----------|
|0 |0-63| 1024| F0 18 0A dd 00 00 08 F7| 
|1| 64-127| 1025|F0 18 0A dd 00 01 08 F7|
|2|128-191|1026| F0 18 0A dd 00 02 08 F7|
|3| 192-255| 1027| F0 18 0A dd 00 03 08 F7|
|4 |256-319 |1028 |F0 18 0A dd 00 04 08 F7|
|5| 320-383| 1029 |F0 18 0A dd 00 05 08 F7|
|6| 384-447| 1030 |F0 18 0A dd 00 06 08 F7|
|7| 448-511 |1031| F0 18 0A dd 00 07 08 F7|
|8| 512-575| 1032| F0 18 0A dd 00 08 08 F7|
|9| 576-639| 1033| F0 18 0A dd 00 09 08 F7|
|1| 64-127 |-1| F0 18 0A dd 00 7F 7F F7|
|0| 0-63| -2 |F0 18 0A dd 00 7E 7F F7|
|0-3| 0-255| -3| F0 18 0A dd 00 7D 7F F7|
|4-9| 256-639| -4| F0 18 0A dd 00 7C 7F F7|

### Note 7 - Alternate Tuning
The “user tuning table” allows any key to be tuned to an arbitrary pitch over an 8 octave range. If selected in the preset, an alternate tuning may be achieved by modifying the tuning values from the front panel or downloading a new table into the machine. The table consists of 128 words, corresponding to the MIDI key range, kept in non-volatile memory. Each word is a pitch value expressed in 1/64
semitones, offset from key number 0 (c-2). Therefore, for equal
temperament, each entry in the table would be equal to its key
number times 64.

### Note 8 - Program Mapping
MIDI program changes will normally correspond to internal preset numbers 0-127. However, the user may “re-map” any MIDI program number, assigning it to an arbitrary internal preset. This feature allows any of the internal presets to be selected from a MIDI keyboard controller.

PRESET DATA FORMAT

Preset data is transmitted and received using the following format:

The standard system exclusive header is followed by the preset number (lsb, msb), a 14 bit word for each preset parameter value (lsb, msb) starting at parameter #0 and continuing upward, a one-byte checksum, and the end-of-exclusive byte (F7). The checksum is the modulo 128 sum of all the parameter value bytes; that is, all of the data bytes
following the preset number and before the checksum.

PRESET PARAMETERS

|Parameter No.| Parameter Name| Range|
|-------------|---------------|------|
|0-11| preset name (12 ascii characters)| 32-127|
|12-14| preset link 1-3| 0-511|
|15-18| preset, link1-3 low key| 0-127|
|19-22 |preset, link 1-3 high key |0-127|
|23 |pri instrument |- - -|
|24| pri sound start offset| 0-127|
|25| pri tuning (coarse)| -36 to +36|
|26| pri tuning (fine) |-64 to +64|
|27| pri volume |0-127|
|28 |pri pan |-7 to +7|
|29| pri delay |0-127|
|30| pri low key |0-127|

|Parameter No. |Parameter Name| Range|
|--------------|--------------|------|
|31 |pri high key |0-127|
|32| pri alt. volume attack |0-99|
|33| pri alt. volume hold |0-99|
|34| pri alt. volume decay |0-99|
|35| pri alt. volume sustain |0-99|
|36| pri alt. volume release |0-99|
|37| pri alt. volume envelope on |0-1|
|38| pri solo mode |0-2|
|39| pri chorus |0-15|
|40| pri reverse sound |0-1|
|41| sec instrument - - -|See Note 9|
|42| sec sound start offset |0-127|
|44| sec tuning (fine)| -64 to +64|
|43| sec tuning (coarse)| -36 to +36|
|45| sec volume| 0-127|
|46| sec pan| -7 to +7|
|47| sec delay| 0-127|
|48| sec low key| 0-127|
|49| sec high key| 0-127|
|50| sec alt. volume attack |0-99|
|51| sec alt. volume hold |0-99|
|52| sec alt. volume decay |0-99|
|53| sec alt. volume sustain |0-99|
|54| sec alt. volume release |0-99|
|55| sec alt. volume envelope on |0-1|
|56| sec solo mode |0-2|
|57| sec chorus |0-15|
|58| sec reverse sound |0-1|
|59| crossfade mode |0-2|
|60| crossfade direction |0-1|
|61| crossfade balance |0-127|
|62| crossfade amount |0-255|
|63| switch point |0-127|
|64| LFO 1 shape |0-8|

|Parameter No. |Parameter Name| Range|
|--------------|--------------|------|
|65| LFO 1 rate |0-127|
|66| LFO 1 delay |0-127|
|67| LFO 1 variation |0-127|
|68| LFO 1 amount -128 |to +127|
|69| LFO 2 shape |0-8|
|70| LFO 2 rate |0-127|
|71| LFO 2 delay |0-127|
|72| LFO 2 variation |0-127|
|73| LFO 2 amount -128 |to +127|
|74| aux. envelope delay |0-127|
|75| aux. envelope attack |0-99|
|76| aux. envelope hold |0-99|
|77| aux. envelope decay |0-99|
|78| aux. envelope sustain |0-99|
|79| aux. envelope release |0-99|
|80| aux. envelope amount |-128 to +127|
|81|-86 key/vel source 1-6 0-1  |See Note 10|
|87-92 |key/vel dest 1-6| 0-42|
|93-98| key/vel amount 1-6| -128 to +127|
|99-106| realtime source 1-8 0-9         |See Note 10|
|107-114| realtime dest 1-8| See list|
|115-117| footswitch dest 1-3 |0-10|
|118-121| controller amount A-D|-128 to +127|
|122| pressure amount |-128 to +127|
|123| pitch bend range|0-13      A value of 13 = “Global”|
|124| velocity curve |0-5             A value of 5 = “Global”|
|125| keyboard center| 0-127|
|126 |submix |0-2|
|127| keyboard tuning |0-5|
|128| pri portamento rate |0-127|
|129| sec portamento rate |0-127|
|130| pri filter type |0-17|
|131| pri filter Fc |0-255|
|132| pri filter Q |0-15|
|133| sec filter type |0-17|
|134| sec filter Fc |0-255|
|135| sec filter Q |0-15|

|Parameter No.| Parameter Name |Range|
|-------------|----------------|-----|
|256| MIDI basic channel |0-15|
|257| MIDI volume (basic channel) |0-127|
|258| MIDI pan (basic channel)| -8 to +7    A value of -8 = “P”, Entire message to set pan to P:```F0 18 0A dd 03 02 02 78 7F F7```|
|259| current preset (basic channel) |0-639|
|260| master tune |-64 to +64|
|261| transpose |-12 to +12|
|262| global pitch bend range| 0-12|
|263| global velocity curve |0-4|
|264| MIDI mode |0-3|
|265| MIDI overflow |0-1|
|266-269| controller A-D numbers |0-31|
|270-272| footswitch 1-3 numbers |64-79|
|273| mode change enable |0-1|
|274| device ID number |0-15|
|336| Global Tempo |0-255                      A value of 0 = External Clock|
|337| Song start/stop enable| 0-1|
|338| Beat/Song number |0-127|
|339| Beat/Song transpose (x factor)| ±36|
|340| Beat/Song mode |0-3|
|341 X factor up|0-34                       0-31, 32=Mpr, 33= Pwh,34 = off|
|342| X factor down| 0-34                     0-31, 32=Mpr, 33= Pwh,34 = off|
|343| Beat MIDI Out| 0-2|
|344| Beat mute Key |0-127|
|345| Beat start key |0-127|
|346| Beat stop key |0-127|
|347| Retrigger channel |0-15|
|348| Retrigger rate |0-49                    See Note 12|
|349| Tempo up| 0-34                         0-31, 32=Mpr, 33= Pwh, 34 = off|
|350| Tempo down| 0-34                       0-31, 32=Mpr, 33= Pwh, 34 = off|
|351| Pitch wheel scratch channel| 0-17      0=None, 1-16 chan, 17=All|
|352| Pitch wheel scratch amount|1-100|
===== Next 4 Per midi channel
|367-383| MIDI channel bank| 0-4|
|384-399| MIDI channel enable |0-1|
|400-415| MIDI program change enable |0-1|
|416-431| mix out |0-3|
======
|512-639| MIDI program/preset map |0-639|

### Note 9 - Sound Sets
A Orbit sound set consists of sample data (sound ROMs), plus additional instrument data in the program ROMs. Each sound set has a unique ID number.

* The sound sets for Orbit are #11 & #12.
* It is necessary to include the sound set number as part of the instrument number when exchanging data.
* The complete instrument number contains two fields: 
  * bits 8-12 specify the sound set (0-31)
  * bits 0-7 specify the instrument within the sound set (0-255)

Instrument Bit Fields:
```
12            8 7                    0
<— sound set —> <——— instrument # ———>
    (5 bits)          (8 bits)
```
Within any given sound set, the first instrument is #1 and #0 selects “None”.

The “magic numbers” 2816 & 3072 represents the start number for
the two banks of Orbit instruments. To calculate the complete instrument number follow the instructions below.

SysEx Instr. No. (Bank 1) = 2816 + Orbit Instr. No.
Next you must convert the SysEx instrument number to a 14-bit MIDI
number. See the information on the following pages.
### Magic Numbers
2816 = Instruments 1-236

3072 = Instruments 237-384

Example:

    Suppose we want to change the instrument to I003 Bass Hum
    1) 2816 + 3 = 2819 (2816 + Instr. No.)
    2) 2819 ÷ 128 = 22 r-3 = 22 (ignore remainder)
    3) 22 in Hex = 16 = msb
    4) remainder 3 in Hex = 03 = lsb
    5) SysEx Instrument Number = lsb msb 03 16
    

The complete message to change the primary instrument to #03:

    F0 18 0A dd 03 17 00 03 16 F7

### 14-bit Signed 2's Complement Numbers

If the data value is negative, you must first take the 2's complement of the number:

In the case of a 14-bit number this is equivalent to adding 16384 to the original negative value.

To fit the 7-bit MIDI protocol, numbers must be “nibble-ized”.
To get the 14-bit nibble-ized value (of a positive value or a 2's complemented negative value):

* msb = value DIV 128 (divide and ignore the remainder)
* lsb = value MOD 128 (divide and use only the remainder)

To go the other way (convert 14 bit signed 2's complement to a signed real number)

    raw Value = (msb*128) + lsb (gives you the unsigned raw value)
    if raw Value ≥ 8192 (8192 = 2^13)
    then signed Value = raw value - 16384 (16384 = 2^14)

Example:

    To find the “nibble-ized” Hex value of -127:
    1) -127 + 16384 = 16252
    2) 16252 ÷ 128 = 126 r-124
    3) 126 in Hex = 7E = msb
    4) 124 in Hex = 7C = lsb
    5) Parameter value would be transmitted as 7C 7E
Example:

    To find the “nibble-ized” Hex value of parameter number 257:
    1) 257 ÷ 128 = 2 r-1
    2) 2 in Hex = 02 = msb
    3) 1 in Hex = 01 = lsb
    4) Parameter number would be transmitted as 01 02

### Note 10 - Patchcord Destinations

The order in which patchcord destinations appear on the screen does not necessarily match the SysEx ordering. This is necessary for various reasons, one being to maintain Proteus compatibility.

Key/Velocity Controllers
|MIDI value| Destination|
|----------|------------|
|0 |Off|
|1| Pitch|
|2 |Pri. Pitch|
|3| Sec. Pitch|
|4 |Volume|
|5| Pri. Volume|
|6 |Sec. Volume|
|7| Attack|
|8| Pri. Attack|
|9| Sec. Attack|
|10 |Decay|
|11| Pri. Decay|
|12 |Sec. Decay|
|3 |Release|
|14| Pri. Release|
|15| Sec. Release|
|16| Crossfade|
|17| LFO 1 Amount|
|18| LFO 1 Rate|
|19| LFO 2 Amount|
|20| LFO 2 Rate|
|21| Aux. Envelope Amount|
|22| Aux. Envelope Attack|
|23| Aux. Envelope Decay|
|24| Aux. Envelope Release|
|25| Sound Start|
|26| Pri. Sound Start|
|27| Sec. Sound Start|
|28| Pan|
|29| Pri. Pan|
|30| Sec. Pan|
|31| Tone|
|32 |Pri. Tone|
|33| Sec. Tone|
|34 |Filter Fc|
|35| Pri. Filter Fc|
|36| Sec. Filter Fc|
|37| Filter Q|
|38| Pri. Filter Q|
|39| Sec. Filter Q|
|40| Portamento Rate|
|41| Pri. Portamento Rate|
|42| Sec. Portamento Rate|

Realtime Controllers

|MIDI value| Destination|
|----------|------------|
|0| Off|
|1| Pitch|
|2| Pri. Pitch|
|3| Sec. Pitch|
|4| Volume|
|5| Pri. Volume|
|6| Sec. Volume|
|7| Attack|
|8| Pri. Attack|
|9| Sec. Attack|
|10| Decay|
|11| Pri. Decay|
|12| Sec. Decay|
|13| Release|
|14| Pri. Release|
|15| Sec. Release|
|16| Crossfade|
|17| LFO 1 Amount|
|18| LFO 1 Rate|
|19| LFO 2 Amount|
|20| LFO 2 Rate|
|21| Aux. Envelope Amount|
|22| Aux. Envelope Attack|
|23| Aux. Envelope Decay|
|24| Aux. Envelope Release|
|28| Pan|
|29| Pri. Pan|
|30| Sec. Pan|
|34| Filter Fc|
|35| Pri. Filter Fc|
|36| Sec. Filter Fc|
|40| Portamento Rate|
|41| Pri. Portamento Rate|
|42| Sec. Portamento Rate|
