package com.github.barriosnahuel.vossosunboton

import android.os.Build
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.LOLLIPOP, Build.VERSION_CODES.P], application = TestApplication::class)
internal abstract class AbstractRobolectricTest
