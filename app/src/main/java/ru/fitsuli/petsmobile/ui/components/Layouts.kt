package ru.fitsuli.petsmobile.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import timber.log.Timber

/**
 * Created by Dmitry Danilyuk at 16.11.2022
 */

val DefaultShape get() = RoundedCornerShape(16.dp)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefLargeTopAppBar(
    title: String,
    onBackClicked: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
) = LargeTopAppBar(
    title = { Text(text = title) },
    navigationIcon = {
        if (onBackClicked != null) {
            IconButton(onClick = onBackClicked) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Arrow back",
                )
            }
        }
    },
    actions = actions,
    scrollBehavior = scrollBehavior,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberCollapsableScrollBehavior(): TopAppBarScrollBehavior {
    return TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleScaffold(
    headerText: String,
    modifier: Modifier = Modifier,
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    onBackPressed: (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    val scrollBehavior = rememberCollapsableScrollBehavior()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            DefLargeTopAppBar(
                title = headerText,
                onBackClicked = onBackPressed,
                scrollBehavior = scrollBehavior
            )
        }, bottomBar = bottomBar,
        snackbarHost = snackbarHost,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoundedSurface(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = DefaultShape,
    color: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(color),
    tonalElevation: Dp = 2.dp,
    shadowElevation: Dp = 0.dp,
    border: BorderStroke? = null,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) = Surface(
    onClick = onClick,
    modifier = modifier,
    shape = shape,
    color = color,
    enabled = enabled,
    contentColor = contentColor,
    tonalElevation = tonalElevation,
    shadowElevation = shadowElevation,
    border = border,
    content = content
)

@Composable
fun RoundedSurface(
    modifier: Modifier = Modifier,
    shape: Shape = DefaultShape,
    color: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(color),
    tonalElevation: Dp = 2.dp,
    shadowElevation: Dp = 0.dp,
    border: BorderStroke? = null,
    content: @Composable () -> Unit
) = Surface(
    modifier = modifier.fillMaxWidth(),
    shape = shape,
    color = color,
    contentColor = contentColor,
    tonalElevation = tonalElevation,
    shadowElevation = shadowElevation,
    border = border,
    content = content
)

@Composable
fun YandexMapKit(
    latitude: Double,
    longitude: Double,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val centerPoint by remember {
        derivedStateOf { Point(latitude, longitude) }
    }
    val mapView = remember {
        MapView(context)
    }

    AndroidView(factory = {
        mapView
    }, update = {
        it.map.move(
            CameraPosition(centerPoint, 11f, 0f, 0f),
            Animation(Animation.Type.SMOOTH, 500f),
            null
        )
    }, modifier = modifier.fillMaxWidth())


    LaunchedEffect(Unit) {
        if (!isMapKitInitialized) {
            MapKitFactory.initialize(context)
            isMapKitInitialized = true
        }
    }

    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    mapView.onStart(); Timber.d("onStart")
                }

                Lifecycle.Event.ON_STOP -> mapView.onStop()
                else -> {}
            }
        }
        lifecycle.addObserver(observer)

        onDispose {
            mapView.onStop()
            lifecycle.removeObserver(observer)
        }
    }
}

var isMapKitInitialized = false