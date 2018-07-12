/*
 * Copyright 2018 Google LLC
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

package com.google.experimental.examples.kotlin.client

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.google.cloud.language.v1.Document
import com.google.cloud.language.v1.EncodingType
import com.google.cloud.language.v1.LanguageServiceClient
import com.google.experimental.examples.kotlin.R
import com.google.experimental.examples.kotlin.util.OnMainThread
import com.google.kgax.grpc.enqueue

/**
 * Kotlin example calling the language API.
 *
 * This example is the same as [MainActivity] but it uses [enqueue] instead of the `get`
 * methods to avoid using an AsyncTask.
 */
class MainActivityEnqueue : AppCompatActivity() {

    private val client by lazy {
        applicationContext.resources.openRawResource(R.raw.sa).use {
            LanguageServiceClient.fromServiceAccount(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView: TextView = findViewById(R.id.text_view)

        val call = client.analyzeEntities(Document.newBuilder()
                .setContent("Hi there Joe")
                .setType(Document.Type.PLAIN_TEXT)
                .build(), EncodingType.UTF8)

        call.enqueue(OnMainThread) { textView.text = "The API says: ${it.body}" }
    }

    override fun onDestroy() {
        super.onDestroy()

        client.shutdownChannel()
    }

}
