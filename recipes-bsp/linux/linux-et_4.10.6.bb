DESCRIPTION = "Linux kernel for ${MACHINE}"
SECTION = "kernel"
LICENSE = "GPLv2"

KERNEL_RELEASE = "4.10.6"
COMPATIBLE_MACHINE = "et4x00|et8000|et10000"

inherit kernel machine_kernel_pr

MACHINE_KERNEL_PR_append = ".1"

SRC_URI[mips.md5sum] = "3c42df14db9d12041802f4c8fec88e17"
SRC_URI[mips.sha256sum] = "738896d2682211d2079eeaa1c7b8bdd0fe75eb90cd12dff2fc5aeb3cc02562bc"

LIC_FILES_CHKSUM = "file://${WORKDIR}/linux-4.10.12/COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

# By default, kernel.bbclass modifies package names to allow multiple kernels
# to be installed in parallel. We revert this change and rprovide the versioned
# package names instead, to allow only one kernel to be installed.
PKG_kernel-base = "kernel-base"
PKG_kernel-image = "kernel-image"
RPROVIDES_kernel-base = "kernel-${KERNEL_VERSION}"
RPROVIDES_kernel-image = "kernel-image-${KERNEL_VERSION}"

SRC_URI += "http://downloads.mutant-digital.net/linux-4.10.12-${ARCH}.tar.gz;name=${ARCH} \
	file://kernel-sublevel-reduce-to-6.patch \
	file://0001-genet1-1000mbit.patch \
	file://bcmgenet_phyaddr.patch \
	file://defconfig \
"

S = "${WORKDIR}/linux-4.10.12"

export OS = "Linux"
KERNEL_OBJECT_SUFFIX = "ko"

# MIPSEL
KERNEL_IMAGETYPE_mipsel = "vmlinux.gz"
KERNEL_OUTPUT_DIR_mipsel = "."
KERNEL_IMAGEDEST_mipsel = "tmp"
KERNEL_CONSOLE_mipsel = "null"
SERIAL_CONSOLE_mipsel ?= ""

KERNEL_EXTRA_ARGS_mipsel = "EXTRA_CFLAGS=-Wno-attribute-alias"

# Replaced by kernel_output_dir
KERNEL_OUTPUT_mipsel = "vmlinux.gz"

pkg_postinst_kernel-image_mipsel() {
	if [ "x$D" == "x" ]; then
		if [ -f /${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE} ] ; then
			flash_eraseall /dev/mtd1
			nandwrite -p /dev/mtd1 /${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}
		fi
	fi
	true
}
