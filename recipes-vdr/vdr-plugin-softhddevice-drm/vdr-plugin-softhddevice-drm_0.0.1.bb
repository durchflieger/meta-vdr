PR = "r0"
MAINTAINER = "df"
SECTION = "multimedia"

PLUGIN_BASE_NAME = "${@d.getVar('PN').replace('vdr-plugin-', '')}"
PLUGIN_CONF_FILE = "50-${PLUGIN_BASE_NAME}.conf"

SUMMARY = "VDR plugin that provides a software and GPU emulated HD device"

DESCRIPTION = "This plugin provides a software and GPU emulated HD device"

HOMEPAGE = "https://github.com/zillevdr/${PN}"

LICENSE = "AGPLv3"
LIC_FILES_CHKSUM = "file://AGPL-3.0.txt;md5=c959e6f173ca1387f63fbaec5dc12f99"

SRC_URI = "git://github.com/zillevdr/vdr-plugin-softhddevice-drm.git;branch=drm;protocol=https"

SRCREV = "ded8dc35b013e6417d3452cbb83b0695a3592a25"

S = "${WORKDIR}/git"

inherit gettext

DEPENDS = "pkgconfig vdr alsa-lib ffmpeg libdrm"

PACKAGES = "${PN}-dbg ${PN} ${PN}-doc"

RDEPENDS:${PN} =+ "vdr"

FILES:${PN} =+ "${libdir}/vdr/plugins"
FILES:${PN} =+ "${datadir}/locale"

CONFFILES:${PN} =+ "${sysconfdir}/vdr/conf.d/${PLUGIN_CONF_FILE}"

do_install () {
  oe_runmake install DESTDIR=${D}

  install -D -m 0644 -t ${D}${docdir}/${PN} ${S}/README

  echo "[${PLUGIN_BASE_NAME}]" > ${WORKDIR}/${PLUGIN_CONF_FILE}
  install -D -m 0644 -t ${D}${sysconfdir}/vdr/conf.d ${WORKDIR}/${PLUGIN_CONF_FILE}
}
