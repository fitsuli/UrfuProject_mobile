package ru.fitsuli.petsmobile.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

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
fun DefTopAppBar(
    title: String,
    onBackClicked: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
) = TopAppBar(
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
    useLargeTopAppBar: Boolean = true,
    actions: @Composable RowScope.() -> Unit = {},
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
            if (useLargeTopAppBar) {
                DefLargeTopAppBar(
                    title = headerText,
                    onBackClicked = onBackPressed,
                    actions = actions,
                    scrollBehavior = scrollBehavior
                )
            } else {
                DefTopAppBar(
                    title = headerText,
                    onBackClicked = onBackPressed,
                    actions = actions,
                    scrollBehavior = scrollBehavior
                )
            }
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
fun GoogleMaps(
    center: LatLng,
    modifier: Modifier = Modifier,
    onMapReady: () -> Unit = {},
    content: (@Composable @GoogleMapComposable () -> Unit)? = null
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(center, 11f)
    }
    val mapProperties by remember {
        mutableStateOf(
            MapProperties(
                isBuildingEnabled = true
            )
        )
    }
    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(mapToolbarEnabled = false)
        )
    }
    Box(modifier = modifier.fillMaxSize()) {
        GoogleMap(
            properties = mapProperties, uiSettings = mapUiSettings,
            onMapLoaded = onMapReady,
            cameraPositionState = cameraPositionState, content = content
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToggleButtonAnim(
    onClick: () -> Unit,
    checked: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    checkedColor: Color = MaterialTheme.colorScheme.primary,
    onCheckedColor: Color = MaterialTheme.colorScheme.onPrimary,
    uncheckedColor: Color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
    onUncheckedColor: Color = MaterialTheme.colorScheme.onBackground,
    shape: Shape = RoundedCornerShape(25),
    border: BorderStroke? = null,
    useSmallPadding: Boolean = false,
    fillInnerWidth: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit
) {
    val animateColor by animateColorAsState(
        targetValue = if (checked) checkedColor else uncheckedColor,
        animationSpec = tween(300)
    )
    val animateContentColor by animateColorAsState(
        targetValue = if (checked) onCheckedColor else onUncheckedColor,
        animationSpec = tween(300)
    )

    val contentPadding = if (useSmallPadding) PaddingValues(
        horizontal = 0.dp,
        vertical = 8.dp
    ) else PaddingValues(
        horizontal = 24.dp,
        vertical = 20.dp
    )

    Surface(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        color = animateColor,
        contentColor = animateContentColor,
        tonalElevation = 0.dp,
        border = border,
        interactionSource = interactionSource
    ) {
        CompositionLocalProvider(LocalContentColor provides animateContentColor) {
            ProvideTextStyle(value = MaterialTheme.typography.labelLarge) {
                Row(
                    Modifier
                        .defaultMinSize(
                            minWidth = ButtonDefaults.MinWidth,
                            minHeight = ButtonDefaults.MinHeight
                        )
                        .padding(contentPadding)
                        .run { if (fillInnerWidth) fillMaxWidth() else this },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    content = content
                )
            }
        }
    }
}