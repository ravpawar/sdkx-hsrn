//
//  RNHelpshift.m
//  Helpshift_react_native
//
//  Created by Ravikumar Pawar on 22/05/22.
//


#import "RNHelpshift.h"
#import "React/RCTBridgeModule.h"
#import "React/RCTEventEmitter.h"
#import "React/RCTLog.h"
#import "React/RCTViewManager.h"
@import HelpshiftX;

@implementation RNHelpshift


RCT_EXPORT_MODULE()

//Helpshift Methods

RCT_EXPORT_METHOD(init:(NSString *)appId domain:(NSString *)domain )
{
  NSDictionary *config = @{};
  [Helpshift installWithPlatformId:appId domain:domain config:config];
}

RCT_EXPORT_METHOD(login:(NSDictionary *)user)
{
  NSDictionary *userDetails = @{ HelpshiftUserName:@"name1",
                                HelpshiftUserEmail:@"email1@email.com",
                           HelpshiftUserIdentifier:@"id1" };
  
  [Helpshift loginUser:userDetails];
   
}

RCT_EXPORT_METHOD(logout)
{
    [Helpshift logout];
}

RCT_EXPORT_METHOD(showConversation)
{
    UIViewController *rootController = UIApplication.sharedApplication.delegate.window.rootViewController;
    [Helpshift showConversationWith:rootController config:nil];
}

RCT_EXPORT_METHOD(showFAQs)
{
    UIViewController *rootController = UIApplication.sharedApplication.delegate.window.rootViewController;
    [Helpshift showFAQsWith:rootController config:nil];
}

RCT_EXPORT_METHOD(showFAQbyId)
{
  UIViewController *rootController = UIApplication.sharedApplication.delegate.window.rootViewController;
  [Helpshift showSingleFAQ:@"3" with:rootController config:nil];
}

@end


@interface RNTHelpshiftManager : RCTViewManager
@property(nonatomic,strong) UIView* helpshiftView;
@end

@implementation RNTHelpshiftManager

RCT_EXPORT_MODULE(RNTHelpshift)

RCT_CUSTOM_VIEW_PROPERTY(config, NSDictionary, RNTHelpshiftManager) {
  
  
}

- (UIView *)view
{
    UIView *view = [[UIView alloc] init];
    self.helpshiftView = view;
    return view;
}

- (dispatch_queue_t)methodQueue
{
  return dispatch_get_main_queue();
}

+ (BOOL)requiresMainQueueSetup
{
    return YES;
}



@end
