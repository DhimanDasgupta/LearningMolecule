// Copyright 2019, Google LLC, Christopher Banes and the Tivi project contributors
// SPDX-License-Identifier: Apache-2.0

package com.dhimandasgupta.core.base

sealed class InvokeStatus
data object InvokeStarted : InvokeStatus()
data object InvokeSuccess : InvokeStatus()
data class InvokeError(val throwable: Throwable) : InvokeStatus()
