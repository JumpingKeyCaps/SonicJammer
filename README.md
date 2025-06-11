
<p align="center">
  <img src="screenshots/appico.png" alt="Logo" width="433" height="433">
</p>


---


# Ultrasonic Audio Jammer - Android

![Platform](https://img.shields.io/badge/platform-Android-green?logo=android)
![Built With](https://img.shields.io/badge/built%20with-Jetpack%20Compose-blue?logo=jetpack-compose)
![Language](https://img.shields.io/badge/language-Kotlin-orange?logo=kotlin)
![Audio API](https://img.shields.io/badge/audio-AudioTrack-%23f06?logo=music)
![IDE](https://img.shields.io/badge/IDE-Android%20Studio-brightgreen?logo=android-studio)
![License](https://img.shields.io/badge/license-MIT-lightgrey)

A stealth Android application that generates a modulated ultrasonic signal to **effectively jam nearby smartphone audio/video recordings**, while remaining inaudible to the human ear.

The ultrasonic jammer leverages a sophisticated decoy strategy designed to exploit the inherent behavior of smartphone microphones and their signal processing algorithms rather than simply drowning out sound with brute force noise. Modern smartphone microphones rely heavily on adaptive digital signal processing (DSP) techniques such as Automatic Gain Control (AGC), noise suppression, and frequency filtering to optimize voice capture and reduce background noise.

By generating a carefully crafted ultrasonic signal — typically between 17 kHz and 19 kHz, beyond the audible range of humans — the jammer emits a dynamic, complex waveform with several key characteristics:

Amplitude Modulation (AM): The slow variation in amplitude (e.g., 5 Hz) imitates natural fluctuations and forces the AGC circuit to continuously readjust gain levels. This prevents the mic from settling on a stable configuration, thereby reducing its ability to accurately capture voice signals.

Frequency Modulation and Chaotic Variations: By adding a small frequency deviation and chaotic frequency shifts, the signal becomes unpredictable and harder for fixed-frequency filters or notch filters to effectively remove. This keeps the microphone’s DSP “off balance,” unable to lock onto a clean baseline.

Random Bursts of Silence: Introducing intermittent bursts where the ultrasonic signal is abruptly turned off prevents the microphone from adapting or filtering out the jammer easily. This pattern breaks the periodicity, making classic filtering and noise suppression techniques less effective.

Together, these elements form a highly irregular, rich ultrasonic noise pattern that overwhelms the microphone’s filtering and gain mechanisms, leading to several detrimental effects on the recording:

The AGC is continuously triggered, causing unnatural gain swings and distortion.

Noise suppression algorithms become confused and may amplify the ultrasonic interference.

Frequency filters struggle to distinguish voice from the ultrasonic decoy signal, leading to signal corruption.

The overall recorded audio becomes distorted, noisy, and unintelligible.

This approach is fundamentally different from traditional jammers that rely on loud audible noise. Instead, it cleverly targets the mic’s adaptive processing behavior by sending inaudible but carefully modulated ultrasonic signals. This hacky “decoy” approach effectively forces the microphone into a compromised, misconfigured state that severely deteriorates the quality of any recorded voice or ambient sound.


---

## 🎯 Project Goal

To create a modulated and chaotic ultrasonic signal (17.3 kHz to 18.3 kHz) capable of interfering with smartphone microphones by disrupting their automatic filters, AGC, and other DSP processing — thus degrading the quality of voice recordings.

---

## 📌 Key Features

- **Target frequency**: between 17.3 kHz and 18.3 kHz (inaudible to most humans)  
- **Combined FM and AM modulation** for a chaotic signal that's hard to filter  
- **Soft distortion (controlled clipping)** to generate disruptive harmonics  
- **Random bursts of silence** to break signal regularity  
- **Optimized for smartphone internal speakers and small external speakers**

---

## ⚙️ Technical Specs

| Parameter            | Default Value                 | Description                                               |
|----------------------|-------------------------------|-----------------------------------------------------------|
| Sample Rate          | 44100 Hz                      | Audio sampling rate                                       |
| Carrier Frequency    | 17500 Hz - 18500Hz            | Main ultrasonic frequency (range for frequency jumping)   |
| Frequency Deviation  | ±500 Hz                       | Slow variation for FM modulation                          |
| AM Modulation        | Depth 0.6, Frequency 5 Hz     | Slow amplitude variation                                  |
| Distortion           | Clipping at 1.2               | To generate harmonics                                     |
| Silence Bursts       | 30% probability               | Random dropouts to increase signal unpredictability       |

---

## 📋 Hardware Compatibility & Limitations

| Device Type                   | Effectiveness | Notes                                      |
|------------------------------|---------------|--------------------------------------------|
| Budget Android smartphones   | ★★★★★         | Less filtering on mic & speaker            |
| High-end Android smartphones | ★★★☆☆         | More aggressive ultrasonic filtering       |
| Modern iPhones               | ★★☆☆☆         | Strong high-pass filtering on microphones  |
| Older iPhones                | ★★★★☆         | Weaker ultrasonic filtering                |

---

## 🚨 Usage Tips & Precautions

- **Legality**: Check your local regulations regarding audio jamming devices.  
- **Hearing Safety**: Avoid prolonged max volume to prevent listening fatigue.  
- **Battery Life**: Continuous jamming consumes significant battery.  
- **Testing**: Always test on the target device before real-world use.

---

## 🔍 Testing Methodology

1. Record audio or video with the target device while jamming is active  
2. Analyze:
   - Waveform distortion  
   - Abnormal voice attenuation or amplification  
   - Presence of inaudible noise/artifacts  

---

## 🧠 Technical Principle

The generated signal is designed to interfere with microphone processing algorithms by:

- Forcing the AGC (automatic gain control) to adapt to a “toxic” signal  
- Disrupting noise suppression and low-cut filters  
- Causing saturation/distortion in the audio chain  

**Result**: Poor-quality, distorted, or even unintelligible voice recordings.


---

## 📸 Screenshots

| Raw decoy | Spectrogram impact |
|:---:|:---:|
| ![RAW](screenshots/demoUi.gif) | ![SPEC](screenshots/demoSpec.gif) |
---

# Special case of SCO mic (Synchronous Connection-Oriented with heavy filter and frequency limit (8khz max))

Most Bluetooth microphones (e.g., AirPods, wireless earbuds) operate using the **SCO profile**, which heavily filters the signal and **limits bandwidth to ~8 kHz**. This makes standard ultrasonic jamming **ineffective**.

However, a secondary technique can be used to disrupt their **AGC** and **VAD** algorithms using sub-ultrasonic decoy bursts.


---

## 🎧 Bluetooth Mic Sabotage Signal — Discreet AGC/VAD Disruption

A specially crafted **signal pattern** designed to subtly interfere with **Bluetooth microphones**
(e.g., AirPods, wireless headsets) during recording or voice calls, without being clearly audible to humans.

### 🎯 Objective

- **Exploit weaknesses** in automatic microphone processing:
  - AGC (Auto Gain Control)
  - VAD (Voice Activity Detection)
  - Noise suppression
- **Inject short bursts** in the upper audible range (~7–8 kHz), just enough to:
  - Trigger gain adjustments
  - Cause misclassification of voice
  - Force momentary suppression or compression

### 🧠 Strategy

- **Burst-based** approach: short sinusoidal spikes (5–20 ms) every few hundred ms.
- **Barely audible but high amplitude**: Not annoying to the ear, but very noticeable for DSP logic.
- **Envelope shaped** (Hann window) to avoid clicks or aliasing.

This creates a low-profile **acoustic decoy**, ideal for disrupting:
- Wireless mic input quality
- Call audio intelligibility
- Smart detection algorithms (especially in Bluetooth chips)

### 🔧 Parameters

| Name              | Type   | Description                             |
|-------------------|--------|-----------------------------------------|
| `sampleRate`      | `Int`  | Audio sampling rate (default 44100)     |
| `durationSeconds` | `Int`  | Total duration of the signal            |
| `burstFrequency`  | `Double` | Frequency in Hz (e.g., 7500-7950)     |
| `burstDurationMs` | `Int`  | Duration of each burst (5–20 ms)        |
| `burstIntervalMs` | `Int`  | Interval between bursts (100–300 ms)    |
| `burstAmplitude`  | `Double` | Burst volume (0.0–1.0)                |

---

⚠️ This technique does **not generate audible noise** in a typical environment,
but it's designed to **interfere acoustically with Bluetooth microphones**
by making their internal DSP logic react suboptimally.

Use responsibly and always verify results with actual devices.

---






