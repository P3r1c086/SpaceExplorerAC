package es.architectcoders.usecases

import es.architectcoders.domain.Apod
import es.architectcoders.domain.NotificationsItem
import es.architectcoders.domain.Photo

internal val samplePhoto = Photo(
    date = "2023-01-01",
    title = "Sample Photo",
    explanation = "A sample photo explanation.",
    hdurl = "https://example.com/hd",
    url = "https://example.com",
    mediaType = "image",
    serviceVersion = "v1",
    type = "photo",
    favorite = false,
    sol = "1",
    imgSrc = "https://example.com/image.jpg",
    id = "1",
    earthDate = "2023-01-01"
)

internal val sampleNotificationsItem = NotificationsItem(
    "2023-01-01",
    "023_AB_123",
    "",
    "https://example.com",
    "FLR"
)

internal val sampleApod = Apod(
    id = "apod1",
    copyright = "NASA",
    date = "2023-01-01",
    explanation = "Explanation",
    hdurl = "https://example.com/hd",
    mediaType = "image",
    serviceVersion = "v1",
    title = "APOD Title",
    url = "https://example.com",
    favorite = true,
    type = "apod"
)