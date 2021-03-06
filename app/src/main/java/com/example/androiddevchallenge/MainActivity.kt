/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.ui.theme.MyTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

// Start building your app here!
@Composable
fun MyApp() {
    val buttonLabel = remember { mutableStateOf("START") }
    val timeText = remember { mutableStateOf("1:00") }
    val num = remember { mutableStateOf(60000L) }
    val finished = remember { mutableStateOf(true) }

    val countNum = object : CountDownTimer(60000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            timeText.value =
                (millisUntilFinished / 1000 / 60).toString() + ":" + (millisUntilFinished / 1000 % 60).toString().padStart(2, '0')
            num.value = millisUntilFinished
        }

        override fun onFinish() {
            timeText.value = "FINISH"
            finished.value = true
        }
    }

    val t = if (timeText.value == "1:00") {
        1f
    } else {
        (num.value - 1000f) / 60000f
    }

    Surface(color = MaterialTheme.colors.background, modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Clock(
                modifier = Modifier
                    .aspectRatio(1.0f)
                    .padding(64.dp),
                t
            )
            Text(
                text = timeText.value,
                fontSize = 60.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )

            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(
                    onClick = {
                        if (buttonLabel.value == "START" && finished.value) {
                            countNum.start()
                            finished.value = false
                            buttonLabel.value = "RESET"
                        } else {
                            countNum.cancel()
                            finished.value = true
                            timeText.value = "1:00"
                            buttonLabel.value = "START"
                            num.value = 60000L
                        }
                    },
                    modifier = Modifier.width(100.dp)
                ) {
                    Text(text = buttonLabel.value)
                }
            }
        }
    }
}

@Composable
fun Clock(modifier: Modifier = Modifier, t: Float) {

    Canvas(modifier = modifier) {
        val middle =
            Offset(size.minDimension / 2, size.minDimension / 2)
        drawCircle(
            color = Color.Black,
            center = middle,
            radius = size.minDimension / 2,
            style = Stroke(4.dp.toPx()),
        )
        withTransform(
            {
                rotate(360 * t, middle)
            },
            {
                drawLine(
                    strokeWidth = 8.dp.toPx(),
                    cap = StrokeCap.Round,
                    color = Color.Red,
                    start = middle,
                    end = Offset(size.minDimension / 2, 12.dp.toPx())
                )
            }
        )

        // To add drawing of hands later.
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}
