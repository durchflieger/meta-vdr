PR = "r0"
MAINTAINER = "df"
SECTION = "multimedia"

PLUGIN_BASE_NAME = "${@d.getVar('PN').replace('vdr-plugin-', '')}"
PLUGIN_CONF_FILE = "50-${PLUGIN_BASE_NAME}.conf"

SUMMARY = "VDR plugin that provides extensive EPG searching capabilities"

DESCRIPTION = "This plugin for the Linux Video Disc Recorder (VDR) allows searching the EPG \
(electronic programme guide) data by defining search terms that can \
permanently be stored in a list for later reuse. It supports regular \
expressions and is capable of doing fuzzy searches. EPG-Search scans the EPG \
in background and can automatically create timers for matching search terms. \
Besides this it supports searching for repetitions, detection of timer \
conflicts, sending emails on timer events and much more. Search terms can \
also be added and modified with vdradmin-am, a web frontend for VDR."

HOMEPAGE = "https://github.com/vdr-projects/vdr-plugin-epgsearch"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=892f569a555ba9c07a568a7c0c4fa63a"

SRC_URI = "git://github.com/vdr-projects/vdr-plugin-epgsearch.git;branch=master;protocol=https \
  file://epgsearchcmds.conf \
  file://epgsearchmenu.conf \
  file://epgsearchcats.conf \
  file://epg2taste.sh \
  file://rememberevent.sh \
"

SRCREV = "76d2b108bf17fde2a98e021c8bbfecb1a9a7e92e"

S = "${WORKDIR}/git"

inherit gettext

#DEPENDS = "pkgconfig vdr libpcre2"
DEPENDS = "pkgconfig vdr"

PACKAGES = "${PN}-dbg ${PN} ${PN}-doc"

RDEPENDS:${PN} =+ "vdr"

FILES:${PN} =+ "${libdir}/vdr/plugins"
FILES:${PN} =+ "${datadir}/locale"
FILES:${PN} =+ "${datadir}/${PN}"

CONFFILES:${PN} =+ "${sysconfdir}/vdr/conf.d"
CONFFILES:${PN} =+ "${sysconfdir}/vdr/plugins/${PLUGIN_BASE_NAME}"

#EXTRA_OEMAKE="AUTOCONFIG=0 PLUGIN_EPGSEARCH_MAX_SUBTITLE_LENGTH=255 REGEXLIB=pcre"
EXTRA_OEMAKE="AUTOCONFIG=0 PLUGIN_EPGSEARCH_MAX_SUBTITLE_LENGTH=255"

do_install () {
  oe_runmake install-lib install-i18n install-conf install-bin DESTDIR=${D}

  install -D -m 0644 -t ${D}${sysconfdir}/vdr/plugins/${PLUGIN_BASE_NAME} ${S}/conf/epgsearchupdmail.templ
  install -D -m 0644 -t ${D}${sysconfdir}/vdr/plugins/${PLUGIN_BASE_NAME} ${S}/conf/epgsearchupdmail-html.templ
  install -D -m 0644 -t ${D}${sysconfdir}/vdr/plugins/${PLUGIN_BASE_NAME} ${S}/conf/epgsearchconflmail.templ
  install -D -m 0644 -t ${D}${sysconfdir}/vdr/plugins/${PLUGIN_BASE_NAME} ${S}/conf/epgsearchcats.conf-epgdata
  install -D -m 0644 -t ${D}${sysconfdir}/vdr/plugins/${PLUGIN_BASE_NAME} ${S}/conf/epgsearchcats.conf-tvm2vdr-hoerzu
  install -D -m 0644 -t ${D}${sysconfdir}/vdr/plugins/${PLUGIN_BASE_NAME} ${S}/conf/epgsearchcats.conf-tvm2vdr-tvmovie

  install -D -m 0644 -t ${D}${sysconfdir}/vdr/plugins/${PLUGIN_BASE_NAME} ${WORKDIR}/epgsearchcmds.conf
  install -D -m 0644 -t ${D}${sysconfdir}/vdr/plugins/${PLUGIN_BASE_NAME} ${WORKDIR}/epgsearchmenu.conf
  install -D -m 0644 -t ${D}${sysconfdir}/vdr/plugins/${PLUGIN_BASE_NAME} ${WORKDIR}/epgsearchcats.conf

  install -D -m 0644 -t ${D}${datadir}/${PN} ${WORKDIR}/epg2taste.sh
  install -D -m 0644 -t ${D}${datadir}/${PN} ${WORKDIR}/rememberevent.sh

  for name in ${PLUGIN_BASE_NAME} quickepgsearch conflictcheckonly epgsearchonly ; do
    echo "[${name}]" > ${WORKDIR}/50-${name}.conf
    install -D -m 0644 -t ${D}${sysconfdir}/vdr/conf.d ${WORKDIR}/50-${name}.conf
  done

  rm ${D}${localstatedir}/lib/vdr/plugins/${PLUGIN_BASE_NAME}/epgsearch*
  ln -sf ${sysconfdir}/vdr/plugins/${PLUGIN_BASE_NAME}/epgsearchupdmail.templ ${D}${localstatedir}/lib/vdr/plugins/${PLUGIN_BASE_NAME}
  ln -sf ${sysconfdir}/vdr/plugins/${PLUGIN_BASE_NAME}/epgsearchconflmail.templ ${D}${localstatedir}/lib/vdr/plugins/${PLUGIN_BASE_NAME}
  ln -sf ${sysconfdir}/vdr/plugins/${PLUGIN_BASE_NAME}/epgsearchcmds.conf ${D}${localstatedir}/lib/vdr/plugins/${PLUGIN_BASE_NAME}
  ln -sf ${sysconfdir}/vdr/plugins/${PLUGIN_BASE_NAME}/epgsearchmenu.conf ${D}${localstatedir}/lib/vdr/plugins/${PLUGIN_BASE_NAME}
  ln -sf ${sysconfdir}/vdr/plugins/${PLUGIN_BASE_NAME}/epgsearchcats.conf ${D}${localstatedir}/lib/vdr/plugins/${PLUGIN_BASE_NAME}
}
