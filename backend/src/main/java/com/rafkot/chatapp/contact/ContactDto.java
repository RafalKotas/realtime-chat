package com.rafkot.chatapp.contact;

import java.util.UUID;

public record ContactDto (
        UUID id,
        String username,
        String imgUrl
) { }
